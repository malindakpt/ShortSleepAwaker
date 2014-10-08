<?php
$con=mysqli_connect("mysql14.000webhost.com","a6673651_user1","1234ab","a6673651_db1");
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }


if (mysqli_query($con,"DELETE FROM groups WHERE group_Name='$_POST[group_Name]'"))
  {
	echo "true";
  }
else
  {
   
  }
  
echo "%%";
mysqli_close($con);
?>