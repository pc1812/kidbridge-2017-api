<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
    <title>${course.name } - kidbridge</title>
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
        body {
            margin-top: 10px;
        }
        .main .container {
            max-width: 670px;
            padding: 0px 10px;
        }
        * {
            max-width: 100%;
        }
        .outline * {
            max-width: 100% !important;
        }
    </style>
</head>
<body>
<div class="main">
    <div class="container">
        <div class="outline">
            ${html eq "<p><br></p>" ? "暂无内容" : html }
        </div>
    </div>
</div>

<script src="//cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
<script>
    $(".outline *").css("width","");
    $(".outline *").css("height","");
    $(".outline *").css("margin","");
    $(".outline *").css("padding","");
</script>
</body>
</html>
