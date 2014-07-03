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
    <div align="right">
      <div>
        <form action="Worker" method="post" name="form1" class="style2" id="form1">
          <label>
          <div align="left">
            <p>Reservation ID
              <input type="text" name="reservationid" value="${param.reservationid}"/>
              <input type="hidden" name="cancelbooking" value="YES"/>			
              <br />
              <input type="submit" class="softbutton" name="Submit" value="Submit" />
            </p>
            <p>${errormessage}  </p>
          </div>
          </form>
      </div>
    </div>
    </div>
</div>
</body>



</html>
<script language="javascript" type="text/javascript">
new Ext.form.DateField().applyTo('datefield');
</script>