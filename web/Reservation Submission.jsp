<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script language="javascript" src="misc.js"></script>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Reservation Form</title>
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
	width:382px;
	height:58px;
	z-index:3;
	left: 580px;
	top: 386px;
}
-->
</style></head>
<body>
<div align="center"><span class="style1">ROOM RESERVATION</span></div>
<div class="style2" id="Layer4">
  <p class="style8"><strong>Reservation Tips:</strong></p>
  <p class="style2">Reservations are valid for 24 hours only. Check in can be made at anytime while check out is 12:00pm the next day. </p>
</div>
<form id="form1" name="reservation_form" method="post" action="Worker" onsubmit="return validateForm2(this);" >
  <div id="Layer1">
    <p class="style9">PERSONAL INFORMATION </p>
    <table width="413" border="0" >
      <tr>
        <td width="189" valign="top" class="style2">Room Type </td>
        <td width="243" valign="top"><input name="roomtype" type="text" disabled="disabled" id="roomtype" size="40" value="${param.roomtype}"/></td>
      </tr>
      <tr>
        <td height="40" valign="top" class="style2">Reservation ID </td>
        <td valign="top"><input name="reservationid" type="text" disabled="disabled" id="reservationid" size="40" value="${reservationid}"/></td>
      </tr>
      <tr>
        <td valign="top" class="style2">Title</td>
        <td valign="top"><select name="title" id="title">
          <option>Mr</option>
          <option>Mrs</option>
          <option>Miss</option>
          <option>Dr</option>
          <option>Engr</option>
        </select>      </td>
      </tr>
      <tr>
        <td valign="top" class="style2">First Name </td>
        <td valign="top"><input name="firstname" type="text" id="firstname" size="40" /></td>
      </tr>
      <tr>
        <td valign="top" class="style2">Last Name </td>
        <td valign="top"><input name="lastname" type="text" id="lastname" size="40" /></td>
      </tr>
      <tr>
        <td height="58" valign="top" class="style2">Address</td>
        <td valign="top">
          
          <textarea name="address" cols="30" rows="3" id="address"></textarea>        </td>
      </tr>
      <tr>
        <td valign="top" class="style2">Phone Number </td>
        <td valign="top"><input name="phonenumber" type="text" id="phonenumber" size="40" /></td>
      </tr>
      <tr>
        <td valign="top" class="style2">Email Address </td>
        <td valign="top"><input name="emailaddress" type="text" id="emailaddress" size="40" placeholder = "anything@yourhost.com" /></td>
      </tr>
    </table>
  </div>
  <div id="Layer2">
    <p class="style9">BOOKING INFO </p>
    <table width="388" height="149" border="2">
      <tr>
        <td width="163" class="style2">Check In Date </td>
        <td width="178" class="style8">${checkindate}</td>
      </tr>
      <tr>
        <td class="style2">Number Of Nights </td>
        <td class="style8">${numberofdays}</td>
      </tr>
      <tr>
        <td class="style2">Number of Rooms </td>
        <td class="style8">${numberofrooms}</td>
      </tr>
      <tr>
        <td class="style2">Price per Room </td>
        <td class="style8">${priceperroom}</td>
      </tr>
      <tr>
        <td class="style2">Total Price </td>
        <td class="style8">${totalprice}</td>
      </tr>
    </table>
  </div>
  <div id="Layer5">
    <div align="center">
      <input name="Submit" type="submit" class="softbuttonbig" value="Submit Booking" />
	  <input name="request_processed" type="hidden" value="${param.request_processed}" />
  	  <input name="hidden_reservationid" type="hidden" value="${reservationid}" />
    </div>
  </div>
</form>
<p>&nbsp;</p>
</body>



</html>
<script language="javascript" type="text/javascript">
new Ext.form.DateField().applyTo('datefield');
</script>