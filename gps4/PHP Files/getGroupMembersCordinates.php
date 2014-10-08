<?php
$con=mysqli_connect("mysql14.000webhost.com","a6673651_user1","1234ab","a6673651_db1");
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }

$result = mysqli_query($con,"SELECT phone,x,y FROM members where group_Name='$_POST[group_Name]'");

while($row = mysqli_fetch_array($result))
  {
  echo $row['phone'] ;	    echo ":"	;
  echo $row['x'] 	;		echo ","	;
  echo $row['y'] 	;		 

  echo ";"				;
  }
echo "%%";
mysqli_close($con);
?>