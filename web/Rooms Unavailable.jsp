<%@ page contentType="text/html; charset=iso-8859-1" language="java" import="java.sql.*" errorPage="" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Untitled Document</title>
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
	left:14px;
	top:64px;
	width:258px;
	height:324px;
	z-index:1;
	visibility: visible;
}
#Layer2 {
	position:absolute;
	left:354px;
	top:65px;
	width:508px;
	height:322px;
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
.style3 {font-family: Arial, Helvetica, sans-serif}
body {
	background-image: url(IMG/body-bg.gif);
}
.style4 {color: #FFFFFF}
.style5 {font-family: Arial, Helvetica, sans-serif; color: #FFFFFF; }
.style6 {font-family: "Lucida Calligraphy"; color: #FFFFFF; }
.style7 {color: #FF0000}
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
</head>
<body>
<div align="center"><span class="style1">NO AVAILABILTY</span></div>
<div class="style2" id="Layer4">
  <p>We are sorry to inform you that your search criteria did not find any aviailable rooms. Please adjust your booking requirements to get available space.</p>
  <form id="form1" name="form1" method="post" action="Worker">
    <input name="roomtype" type="hidden" value="${param.roomtype}" />
	<input name="rooms_unavailable" type="hidden" value="${param.rooms_unavailable}" />
    <label>
    <input name="Submit" class="softbutton" type="submit" value="Back To Booking" />
    </label>    
  </form>
  </div>
</body>



</html>
<script language="javascript" type="text/javascript">
new Ext.form.DateField().applyTo('datefield');
</script>