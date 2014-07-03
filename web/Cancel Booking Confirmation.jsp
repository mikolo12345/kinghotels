<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script language="javascript" src="misc.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Reservation Successful</title>
<style type="text/css">
<!--
.style1 {
	font-family: "Lucida Calligraphy";
	font-size: 24px;
	font-weight: bold;
	color: #FF0000;
}
#Layer1 {
	position:absolute;
	left:28px;
	top:145px;
	width:442px;
	height:324px;
	z-index:1;
	visibility: visible;
}
#Layer2 {
	position:absolute;
	left:574px;
	top:146px;
	width:366px;
	height:184px;
	z-index:2;
}
#Layer3 {
	position:absolute;
	left:920px;
	top:71px;
	width:199px;
	height:320px;
	z-index:3;
}
.style2 {
	font-family: "Lucida Calligraphy";
	color: #FFFFFF;
}
.style3 {
	font-family: "Lucida Calligraphy";
	font-size:12px;
	color: #FFFFFF;
}
body {
	background-image: url(IMG/body-bg.gif);
}
.style8 {font-family: "Lucida Calligraphy"; color: #FF0000; }
.reservation
{
	font-family: "Verdana";
	font-size: 14px;
	font-weight: normal;
	font-variant: normal;
	color: #FFFFFF;
	margin: 0px;
	background-color: #FF0000;
}
-->
</style>
<link href="CSS/main.css" rel="stylesheet" type="text/css" />
<style type="text/css">
<!--
.style9 {font-family: "Lucida Calligraphy"; color: #FF0000; font-weight: bold; }
#Layer5 {
	position:absolute;
	width:335px;
	height:58px;
	z-index:3;
	left: 580px;
	top: 386px;
}
#Layer6 {
	position:absolute;
	width:287px;
	height:115px;
	z-index:1;
	left: 32px;
	top: 152px;
}
-->
</style></head>
<body>
<div align="center"><span class="style1">CANCEL A BOOKING </span></div>
<div class="style2" id="Layer4">
  <p class="style2">Reservations are valid for 24 hours only and hence would be automatically canceled if not paid for before deadline however, you may also cancel it 
    manually. <br />
  </p>
</div>
<div id="parent_div">
  <div id="div_for_print">
    <p align="center" class="style9">BOOKING DETAILS </p>
    <div align="right">
      <div id="Layer6">
        <form action="Worker" method="post" name="form1" class="style2" id="form1">
          <label>
          <div align="left">
            <input type="hidden" name="cancelbookingconfirmation" value="YES"/>		  			
            <br />
            <input type="submit" class="softbutton" name="Submit" value="Cancel Booking" />
          </div>
          </label>
        </form>
      </div>
      <table  width="544">
        <tr>
          <td width="184" class="style2"><div align="justify">Reservation ID</div></td>
          <td width="348" class="style2">:${reservationid}</td>
        </tr>
        <tr>
          <td class="style2">Room Type </td>
          <td class="style2">:${roomtype}</td>
        </tr>
        <tr>
          <td class="style2">Check In Date </td>
          <td class="style2"><span class="style2">:${checkindate}</span></td>
        </tr>
        <tr>
          <td class="style2">Number of Nights </td>
          <td class="style2"><span class="style2">:${numberofdays}</span></td>
        </tr>
        <tr>
          <td class="style2">Number of Rooms </td>
          <td class="style2"><span class="style2">:${numberofrooms}</span></td>
        </tr>
        <tr>
          <td class="style2">Total Price </td>
          <td class="style2"><span class="style2">:${totalprice}</span></td>
        </tr>
      </table>
    </div>
    <p align="center" class="style9">PERSONAL DETAILS </p>
     <div align="right">
       <table width="545">
         <tr>
           <td width="185" class="style2">Name</td>
           <td width="348" class="style2">:${customername}</td>
         </tr>
         <tr>
           <td class="style2">Phone No </td>
           <td class="style2">:${phonenumber}</td>
         </tr>
         <tr>
           <td class="style2">Email</td>
           <td class="style2">:${emailaddress}</td>
         </tr>
         <tr>
           <td class="style2">Address</td>
           <td class="style2">:${address}</td>
         </tr>
       </table>
     </div>
    <p align="right">&nbsp;</p>
  </div>
</div>
</body>



</html>
<script language="javascript" type="text/javascript">
new Ext.form.DateField().applyTo('datefield');
</script>