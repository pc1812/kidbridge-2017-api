<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">

    <title>${article.title }</title>

    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.7/css/bootstrap-theme.min.css">

    <style type="text/css">
        .text {
            padding-top: 10px;
        }
        .title {
            font-size: 18px;
            font-weight: bold;
            padding-top: 10px;
        }
        .date {
            color: #999999;
            padding-top: 10px;
        }
        .main{
            padding-top: 10px;
            max-width: 640px;
        }
        .content * {
            width: 100% !important;
        }
    </style>

    <!--[if lt IE 9]>
        <script src="//cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->

</head>
<body>

    <div class="container main">
        <div class="title">
            ${article.title }
        </div>
        <div class="date">
            <fmt:formatDate value="${article.createTime }" pattern="yyyy/MM/dd" />
        </div>
        <div class="text">
            <div class="content">

            </div>
        </div>
    </div>

    <script src="//cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script>
        $.ajax({
            url: "http://${bucketDomain }/${article.content }",
            type: "GET",
            dataType: "text",
            success: function(res){
                $(".content").html(res);
                $(".content *").css("width","");
                $(".content *").css("height","");
                $(".content *").css("margin","");
                $(".content *").css("padding","");
            }
        });
    </script>
</body>
</html>