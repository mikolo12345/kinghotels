/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package servlets;

import db.DatabaseManager;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialException;
import javax.swing.text.NumberFormatter;
import misc.BookingRequest;
import misc.CustomerInfo;
import misc.Utils;

/**
 *
 * @author HP USER
 */
@WebServlet(name = "Worker", urlPatterns = {"/Worker"})
public class Worker extends HttpServlet {
    private final static String STANDARD = "STANDARD", VIP = "VIP", VVIP = "VVIP";
    private DatabaseManager dbm;
    private Connection connection;
    SimpleDateFormat sdf1, sdf2;
    DecimalFormat df;
    private Map<String, BookingRequest> reservationMap; 

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        sdf1 = new SimpleDateFormat("yyyy/MM/dd");
        sdf2 = new SimpleDateFormat("dd MMM, yyyy");
        df = new DecimalFormat("NGN #,##0.00");
        dbm = new DatabaseManager();
        reservationMap = new HashMap<String, BookingRequest>();
        
        try {
            connection = dbm.connect();
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("had to first create db");
            dbm.createDatabase();
            try {
                connection = dbm.connect();
            } catch (SQLException ex1) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex1);
                throw new ServletException(ex1);
            }
        }
        // remove any expired bookings from the database
        removeExpiredBookings(connection);
    }

    
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException, SQLException {        
        String requestProcessed = request.getParameter("request_processed");
        
        if (request.getParameter("cancelbooking") != null) {
            String reservationid = request.getParameter("reservationid");
            BookingRequest bookingRequest = getBookingRequest(connection, reservationid);
            if (bookingRequest != null) {
                request.setAttribute("reservationid", reservationid);
                request.setAttribute("roomtype", bookingRequest.getRoomtype());
                request.setAttribute("checkindate", sdf2.format(bookingRequest.getCheckInDate()));
                request.setAttribute("numberofdays", bookingRequest.getNumberOfDays());
                request.setAttribute("numberofrooms", bookingRequest.getNumberOfRooms());
                request.setAttribute("priceperroom", df.format(bookingRequest.getPricePerRoom()));
                request.setAttribute("totalprice", df.format(bookingRequest.getTotalPrice()));
            }

            CustomerInfo customerInfo = getCustomerInfo(connection, reservationid);
            if (customerInfo != null) {
                request.setAttribute("customername", customerInfo.getTitle() + " " + customerInfo.getFirstname() + " " + customerInfo.getLastname());
                request.setAttribute("phonenumber", customerInfo.getPhonenumber());
                request.setAttribute("address", customerInfo.getAddress());
                request.setAttribute("emailaddress", customerInfo.getEmailaddress());
            }
            
            String address;
            if (bookingRequest == null) {
                address = "Cancel Booking.jsp";
                request.setAttribute("errormessage", "Couldn't find any record corresponding to the Reservation ID " + reservationid +
                        ". It may have already expired, been canceled or never existed.");
            }
            else if (removeBooking(connection, reservationid) < 1) {
                address = "Cancel Booking.jsp";
                request.setAttribute("errormessage", "A database error occurred while trying to delete the Reservation ID " + reservationid +
                        ". Please try again later.");
            }
            else {
                address = "Cancel Booking Confirmation.jsp";
            }
            
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(address);
            requestDispatcher.forward(request, response);
            
        }
        else if (request.getParameter("cancelbookingconfirmation") != null) {
            String address = "Cancel Booking Success.html";
            
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(address);
            requestDispatcher.forward(request, response);
        }
        else if (request.getParameter("rooms_unavailable") != null) {// that is the booking request has been processed then go back to previous page
            String roomType = request.getParameter("roomtype");
            String address;
            if (roomType.toUpperCase().equals("STANDARD")) {
                address = "standard room.html";
            }
            else if (roomType.toUpperCase().equals("VIP")) {
                address = "vipBook.html";
            }
            else {
                address = "vvip room.html";
            }
            
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(address);
            requestDispatcher.forward(request, response);
        }
        else if (requestProcessed == null) {
            String roomType = request.getParameter("roomtype");
            String checkindate = request.getParameter("checkindate");              
            int noOfDays = Integer.parseInt(request.getParameter("numdays"));
            int numOfRooms = Integer.parseInt(request.getParameter("numrooms"));
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new java.util.Date());
            
            Date date;
            try {
                date = new Date(sdf1.parse(checkindate).getTime()); //new Date(calendar.getTimeInMillis());
            } catch (ParseException ex) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
                throw new ServletException(ex);
            }
            
            if (roomType != null) { // i.e this is the booking 
                BookingRequest bookingRequest = processBookingRequest(roomType, date, noOfDays, numOfRooms, connection);
                String address;
                if (bookingRequest == null) { // ie there is no available space for the booking eg rooms exhausted
                    address = "Rooms Unavailable.jsp";
                } else {
                    address = "Reservation Submission.jsp";
                    String requestID = Utils.generateUID("KH-", null);
                    reservationMap.put(requestID, bookingRequest);                    
                    request.setAttribute("reservationid", requestID);                    
                    request.setAttribute("checkindate", sdf2.format(bookingRequest.getCheckInDate()));
                    request.setAttribute("numberofdays", bookingRequest.getNumberOfDays());
                    request.setAttribute("numberofrooms", bookingRequest.getNumberOfRooms());                    
                    request.setAttribute("priceperroom", df.format(bookingRequest.getPricePerRoom()));
                    request.setAttribute("totalprice", df.format(bookingRequest.getTotalPrice()));                    
                }

                RequestDispatcher requestDispatcher = request.getRequestDispatcher(address);
                requestDispatcher.forward(request, response);
            } 
        }
        else { // ie fit to save into database            
            boolean success = saveReservation(request, connection);
            String address;
            if (success) {                                
                address = "Reservation Successful.jsp";
            }
            else {
                address = "Rooms Unavailable.jsp";
                //request.("rooms_unavailable", "YES");
            }
            
            RequestDispatcher requestDispatcher = request.getRequestDispatcher(address);
            requestDispatcher.forward(request, response);
        }       
        
    }    
    
    protected void processRequestTest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /*
             * TODO output your page here. You may use following sample code.
             */
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet Worker</title>");            
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet Worker at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        } finally {            
            out.close();
        }
    }
    
    BookingRequest processBookingRequest(String roomType, Date checkInDate, int noOfDays, int numOfRoomsRequested, Connection conn) throws SQLException {
        int totalNumRooms = getTotalNumOfRooms(roomType, conn);
        double pricePerRoom = getPricePerRoom(roomType, conn);
        int numUsedRooms = -1;
        //PreparedStatement pt = conn.prepareStatement("SELECT * FROM BOOKING WHERE "
        //        + "CAST({fn TIMESTAMPADD( SQL_TSI_DAY, NO_OF_DAYS, CHECK_IN_DATE)} AS DATE) >= ? AND "
        //        + "CAST({fn TIMESTAMPADD( SQL_TSI_DAY, ?, ?)} AS DATE)"
        PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM BOOKING WHERE ? BETWEEN CHECK_IN_DATE AND CHECK_OUT_DATE");
        ResultSet rs;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkInDate);
        Date currDate;// = new Date(calendar.getTimeInMillis());
        
        for (int i = 0; i<noOfDays; ++i) {
            currDate = new Date(calendar.getTimeInMillis());
            ps.setDate(1, currDate);
            rs = ps.executeQuery();
            if (rs.next()) {
                numUsedRooms = rs.getInt(1);
                if (numUsedRooms + numOfRoomsRequested > totalNumRooms) {
                    ps.close();
                    return null;
                }
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }   
        
        return new BookingRequest(checkInDate, roomType, noOfDays, numOfRoomsRequested, pricePerRoom);        
    }
    
    private int getTotalNumOfRooms(String roomType, Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT NUMBER_OF_ROOMS FROM ROOM_PRICING WHERE ROOM_TYPE = '" + roomType +"'");   
        rs.next();
        int val = rs.getInt(1);        
        rs.close();
        st.close();
        return val;
        
    }
    
    private int getPricePerRoom(String roomType, Connection conn) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery("SELECT PRICE_PER_ROOM FROM ROOM_PRICING WHERE ROOM_TYPE = '" + roomType +"'");   
        rs.next();
        int val = rs.getInt(1);        
        rs.close();
        st.close();
        return val;        
    }
    
    private boolean saveReservation(HttpServletRequest request, Connection conn) {
        boolean success = false;
        String reservationID = (String) request.getParameter("hidden_reservationid");
        
        String title = request.getParameter("title");
        String firstName = request.getParameter("firstname");
        String lastName = request.getParameter("lastname");
        String address = request.getParameter("address");
        String phoneNumber = request.getParameter("phonenumber");
        String emailaddress = request.getParameter("emailaddress");
        BookingRequest bookingRequest = reservationMap.get(reservationID);       
                
        if (bookingRequest == null) return false;
        
        try {
            // insert into the database using sql
            boolean prevCommitValue = conn.getAutoCommit();
            conn.setAutoCommit(false);
            PreparedStatement ps = conn.prepareStatement("INSERT INTO CUSTOMER (TITLE, FIRST_NAME, LAST_NAME, "
                    + "ADDRESS, PHONE_NUMBER, EMAIL) VALUES(?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, title);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, address);
            ps.setString(5, phoneNumber);
            ps.setString(6, emailaddress);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            rs.next();            
            String generatedCustID = rs.getString(1);
            rs.close();
            ps.close();
            
            
            ps = conn.prepareStatement("INSERT INTO BOOKING "
                    + "(BOOK_ID, TIME_STAMP, CUSTOMER_ID, ROOM_TYPE, CHECK_IN_DATE, CHECK_OUT_DATE, NUMBER_OF_DAYS, "
                    + "NUMBER_OF_ROOMS, TOTAL_COST, PAID) VALUES (?, CURRENT_TIMESTAMP, ?, ?, ?, ?, ?, ?, ?, ?)");
            Calendar c = Calendar.getInstance();
            c.setTime(bookingRequest.getCheckInDate());
            c.add(Calendar.DAY_OF_MONTH, bookingRequest.getNumberOfDays());
            java.util.Date checkoutDate = c.getTime();
            
            ps.setString(1, reservationID);
            ps.setString(2,generatedCustID);
            ps.setString(3, bookingRequest.getRoomtype());
            ps.setDate(4, new Date(bookingRequest.getCheckInDate().getTime()));           
            ps.setObject(5, new Date(checkoutDate.getTime()));
            ps.setInt(6, bookingRequest.getNumberOfDays());
            ps.setInt(7, bookingRequest.getNumberOfRooms());
            ps.setDouble(8, bookingRequest.getTotalPrice());
            ps.setString(9, "NO");
            ps.executeUpdate();            
            ps.close();
            conn.commit();
            conn.setAutoCommit(prevCommitValue);
            success = true;
            
            // set request attributes so as to display the next page properly
            request.setAttribute("reservationid", reservationID);
            request.setAttribute("roomtype", bookingRequest.getRoomtype());
            request.setAttribute("checkindate", sdf2.format(bookingRequest.getCheckInDate()));
            request.setAttribute("numberofdays", bookingRequest.getNumberOfDays());
            request.setAttribute("numberofrooms", bookingRequest.getNumberOfRooms());
            request.setAttribute("priceperroom", df.format(bookingRequest.getPricePerRoom()));
            request.setAttribute("totalprice", df.format(bookingRequest.getTotalPrice()));
            request.setAttribute("amountpaid", df.format(0));
            
            request.setAttribute("customername", title + " " + firstName + " " + lastName);
            request.setAttribute("phonenumber", phoneNumber);
            request.setAttribute("address", address);
            request.setAttribute("emailaddress", emailaddress);
            request.setAttribute("printdate", new java.util.Date().toString());
            
            reservationMap.remove(reservationID);
        } catch (SQLException ex) {
            success = false;
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            try {
                conn.rollback();
            } catch (SQLException ex1) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex1);
            }
        }
        
        return success;
    }
    
    private void removeExpiredBookings(Connection conn) {
        try {
            boolean prevVal = conn.getAutoCommit();
            conn.setAutoCommit(false);
            Statement st = conn.createStatement();
            st.executeUpdate("INSERT INTO EXPIRED_BOOKING SELECT * FROM BOOKING WHERE "
                    + "{fn TIMESTAMPDIFF( SQL_TSI_HOUR, TIME_STAMP, CURRENT_TIMESTAMP)} > 24 AND PAID = 'NO'");
            st.executeUpdate("DELETE FROM BOOKING WHERE "
                    + "{fn TIMESTAMPDIFF( SQL_TSI_HOUR, TIME_STAMP, CURRENT_TIMESTAMP)} > 24 AND PAID = 'NO'");
            conn.commit();
            conn.setAutoCommit(prevVal);
        } catch (SQLException ex) {            
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex1) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
    }
    
    private int removeBooking(Connection conn, String reservationID) {
        int num = -1;
        try {
            boolean prevVal = conn.getAutoCommit();
            conn.setAutoCommit(false);
            Statement st = conn.createStatement();
            num = st.executeUpdate("INSERT INTO EXPIRED_BOOKING SELECT * FROM BOOKING WHERE "
                    + "BOOK_ID = \'" + reservationID + "\'");
            int num2 = st.executeUpdate("DELETE FROM BOOKING WHERE "
                    + "BOOK_ID = \'" + reservationID + "\'");
            conn.commit();
            conn.setAutoCommit(prevVal);
        } catch (SQLException ex) {            
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            try {
                conn.rollback();
                conn.setAutoCommit(true);
            } catch (SQLException ex1) {
                Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex1);
            }
            
        }
        
        return num;
    }
    
    private BookingRequest getBookingRequest(Connection conn, String reservationID) {
        try {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT CHECK_IN_DATE, BOOKING.ROOM_TYPE, NUMBER_OF_DAYS, BOOKING.NUMBER_OF_ROOMS, PRICE_PER_ROOM "
                    + " FROM BOOKING, ROOM_PRICING WHERE BOOK_ID = \'" + reservationID +"\' AND BOOKING.ROOM_TYPE = ROOM_PRICING.ROOM_TYPE");
            if (rs.next()) {            
                BookingRequest bookingRequest = new BookingRequest(rs.getDate(1), rs.getString(2), rs.getInt(3), rs.getInt(4), rs.getDouble(5));
                rs.close(); st.close();
                return bookingRequest;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    private CustomerInfo getCustomerInfo(Connection conn, String reservationID) {
        try {
            Statement st = conn.createStatement();
            
            ResultSet rs = st.executeQuery("SELECT CUSTOMER_ID FROM BOOKING WHERE BOOK_ID = \'" + reservationID + "\'");
            if (!rs.next()) {            
                return null;
            }
            else{
                String custid = rs.getString(1);
                rs = st.executeQuery("SELECT TITLE, FIRST_NAME, LAST_NAME, PHONE_NUMBER, ADDRESS, EMAIL FROM CUSTOMER "
                        + "WHERE CUSTOMER_ID = " + custid + "");
                if (rs.next()) {
                    CustomerInfo customerInfo = new CustomerInfo(rs.getString(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                    rs.close();
                    st.close();
                    return customerInfo;
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            processRequest(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(Worker.class.getName()).log(Level.SEVERE, null, ex);
            throw new ServletException(ex);
        }
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
    
    
}
