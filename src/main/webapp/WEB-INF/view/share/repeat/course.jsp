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
    <link href="/resources/css/plugins/slick/slick.css" rel="stylesheet">
    <link href="/resources/css/plugins/slick/slick-theme.css" rel="stylesheet">
    <link href="//cdn.bootcss.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet">
    <!--[if lt IE 9]>
        <script src="//cdn.bootcss.com/html5shiv/3.7.3/html5shiv.min.js"></script>
        <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style type="text/css">
        .main .container {
            max-width: 670px;
            padding: 0px 10px;
        }
        .book-icon img {
            height: 150px;
            margin: 15px 10px;
            border-radius: 8px;
            border: 1px solid #ddd;
            box-shadow: 0px 5px 10px #ddd;
        }
        .book-icon {
            padding: 0;
            margin: 0;
        }
        .book-icon .slick-center {
            -moz-transform:scale(1.08);
            -ms-transform:scale(1.08);
            -o-transform:scale(1.08);
            -webkit-transform:scale(1.08);
            /* transform:scale(1.08); */
        }
        .panel {
            margin-bottom: 10px;
        }
        .user-head,.audio div {
            display: inline-block;
        }
        .user-head img {
            height: 40px;
            width: 40px;
            border-radius: 50px;
        }
        .audio {
            background-color: #15cf30;
            line-height: 40px;
            width: calc(100% - 90px);
            color: white;
            padding-left: 15px;
            padding-right: 15px;
            display: inline-block;
            margin-left: 5px;
            margin-right: 2px;
        }
        .player {
            width: 40px;
            text-align: center;
            font-size: 40px;
            line-height: 40px;
            color: #15cf30;
        }



        .top-applink {
            /* display: none; */
            position: relative!important;
            width: 100%;
            height: 49px;
            background-color: rgba(0,0,0,.7);
            overflow: hidden;
            padding: 7px 0;
        }
        .swiper-wrapper {
            -webkit-transform: translateZ(0);
            transform: translateZ(0);
        }
        .swiper-wrapper {
            position: relative;
            width: 100%;
            height: 100%;
            z-index: 1;
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
            -webkit-transition-property: -webkit-transform;
            transition-property: -webkit-transform;
            transition-property: transform;
            transition-property: transform,-webkit-transform;
            box-sizing: content-box;
        }
        .top-applink .swiper-slide {
            display: -webkit-box;
            display: -webkit-flex;
            display: -ms-flexbox;
            display: flex;
        }
        .swiper-slide {
            -webkit-flex-shrink: 0;
            -ms-flex-negative: 0;
            flex-shrink: 0;
            width: 100%;
            height: 100%;
            position: relative;
            -webkit-transition-property: -webkit-transform;
            transition-property: -webkit-transform;
            transition-property: transform;
            transition-property: transform,-webkit-transform;
        }
        .top-applink .btn-banner-close {
            display: block;
            float: left;
            margin-left: 9px;
            margin-top: 10px;
            width: 38px;
            height: 16px;
            text-align: center;
        }
        .top-applink .btn-banner-close i {
            display: inline-block;
            width: 16px;
            height: 16px;
            background: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAADAAAAAwCAYAAABXAvmHAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAyNpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDE0IDc5LjE1MTQ4MSwgMjAxMy8wMy8xMy0xMjowOToxNSAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvIiB4bWxuczp4bXBNTT0iaHR0cDovL25zLmFkb2JlLmNvbS94YXAvMS4wL21tLyIgeG1sbnM6c3RSZWY9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9zVHlwZS9SZXNvdXJjZVJlZiMiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIiB4bXBNTTpJbnN0YW5jZUlEPSJ4bXAuaWlkOkI3QzBDMzUyQjFEMzExRTc4NTY3OENFNTY5NTY3MEQ0IiB4bXBNTTpEb2N1bWVudElEPSJ4bXAuZGlkOkI3QzBDMzUzQjFEMzExRTc4NTY3OENFNTY5NTY3MEQ0Ij4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5paWQ6QjdDMEMzNTBCMUQzMTFFNzg1Njc4Q0U1Njk1NjcwRDQiIHN0UmVmOmRvY3VtZW50SUQ9InhtcC5kaWQ6QjdDMEMzNTFCMUQzMTFFNzg1Njc4Q0U1Njk1NjcwRDQiLz4gPC9yZGY6RGVzY3JpcHRpb24+IDwvcmRmOlJERj4gPC94OnhtcG1ldGE+IDw/eHBhY2tldCBlbmQ9InIiPz72N+WoAAAEWklEQVR42tSab2iNURzH770zWgzZRi5vvLBsL2xG/rzklbpiIokRWdOUYkarjbBIiYQVCWONMt4yb7b2xsyfjUn+bEUp8i9ts2Hk+p76Hf38eu59nuc8z727Tn3qPs89zznf33nOc/78ficYjUYDPqTJYDFYCGaBGSAHjKX/B8FH8Ao8B3dBK/jgteKgBwOywEawDsxVZbl8XlX8EFwBl8FnIxXKAJdMBcfA16h/aRCcAmG3ety8gVFgJ9gHxon/hqlbtIBu8BK8o64TpfxTQS6YDZZQdxstyvkKDoIT4Kefb2Am6LRouUdgG5hk8CbVM+VUhkxdIN9JOU4qWgH6RAXKmGUgaCBcEqSynog6BkCxVwPKwC9W6DdQAdJ8EC4ZBSqpDp1+kQYjA8pFi/SCggQIl8wGPaLucrcGLBct/wBMSYJ4zWSqk7+J5U4NyKP+p1MHmJBE8RpV513xTeTZGZAuRgXVbXJGQLwmhzTwUS89ngG7WebvoGgExWuKSItOe2IZEBZdpyIFxGt2iq4UtjLgtJhI0lLIgDTSpFOdNCAbDLEMS1NIvGYp0zdEmgMhWlGUgAz63QWaHa6PNoCn4KSHFXEVeA0O2+RrJm0B0lrC10J8zC110So8nTdo1RpRhl3+Upb3vu5CatL4zUaeiS4ENAgBF108e0A82+ZwbtAjktIcVjfXuixEck0IOWvQ8mrCGu2wvjb23Hr1DSxg/azVoA+vATfYdRm4GCd/Nahl1+20Nxh2WF8L+z1PWXSLWVTsYZRoFK16wSJPrcjTblBPMXv+ZkCs/PI9DnXX43wT1eK/O4Z15LMyetSNfnYjy4fxWhqxH2z3oeU1Wayc/mD0303xGBd9MV66DlbF+O8+mO+hbLWP/qEvQoHEpNXgmMX9qx7FyzSsDBhgNzJ9LPyNxb33PpTLNQ6ERKFTfBJfQ64RmXaACx7L5hq/KAN62Y1cH8TXinG+CZxj15tt5gm7xDX2hMhXqVOhDy1fw64f0ERXJia7TeCMYR0F7PcLP5YSsZYHapwPuZgnAgZLiZIAeRtMF3Oagy4mKScztqvFnFxOb3Epfq/BDCvfRL3DurYIV8/f/cAOsZ106jKs8rA8aBLP1jlwQXbJPXusLWXEYEPTYdD1brjY0ESstpQ8Qx3L0E2+SjsBR2gtddthfisuURn1Nn7Tx/E29VZulcoU2tDvcuJWSVXH1hynji3tWuxMYddip51rUUdj+sR+dfwIiM8Uzl31neSautfvJflN5FCdRu71WAEOtfUsTIL4Qj8CHLFCTN//pxAT9wJYBfkiPgb5IjT3yCDfSj+ilPHCrGpq3+ohzLo1TvjWkYfETaBbbaYrab2fYRHobifHmA50v6XAdYAC3WEW6FbnKhZZBLq/gUPgqGPngkHLTQPHE3DUQMUnpifyqIFM2eReXw+KDA97dIJG0AA+Jfu0itxoLyY/ax47bjOOnYHQx22egQ7qbp69FH8EGAC/b/1+0wqJnAAAAABJRU5ErkJggg==') no-repeat 50%/contain;
        }
        .top-applink .app-info {
            font-size: 12px;
            line-height: 15px;
            overflow: hidden;
            padding-right: 10px;
            -webkit-box-flex: 1;
            -webkit-flex: 1;
            -ms-flex: 1;
            flex: 1;
        }
        .top-applink .app-logo {
            float: left;
            margin-right: 10px;
        }
        .top-applink .app-logo img {
            width: 35px;
            height: 35px;
            vertical-align: top;
            border-radius: 5px;
        }
        img {
            font-size: 0;
            border-style: none;
            vertical-align: middle;
        }
        img {
            border: 0;
        }
        .top-applink .app-intro {
            overflow: hidden;
            height: 34px;
            color: #fff;
            font-size: 11px;
            line-height: 17px;
        }
        .top-applink .btn-app-open {
            float: right;
            width: 56px;
            height: 24px;
            margin-right: 17px;
            margin-top: 6px;
            background-color: #f04848;
            border-radius: 4px;
            font-size: 11px;
            color: #fff;
            text-align: center;
            line-height: 24px;
        }
        .qrcode {
            background-color: rgba(0,0,0,.7);
        }
    </style>
</head>
<body>
    <div class="app-download">
        <div class="top-applink">
            <div class="swiper-wrapper">
                <div class="swiper-slide">
                    <%--<a href="javascript:" class="btn-banner-close J_toplink_close">
                        <i class="icon-close"></i>
                    </a>--%>
                    <div class="app-info" style="margin-left: 15px;">
                        <span class="app-logo"><img src="data:image/png;base64,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"></span>
                        <div class="app-intro" style="font-size: 15px;">TA的跟读不错，你也想试试？<br>快去下载APP进行跟读吧 ~</div>
                    </div>
                    <a class="btn-app-open J_ota" href="${header['user-agent'].toLowerCase().contains("iphone") or header['user-agent'].toLowerCase().contains("mac") ? "https://itunes.apple.com/cn/app/hs%E8%8B%B1%E6%96%87%E7%BB%98%E6%9C%AC/id1353729754?mt=8" : "http://res.kidbridge.org/2c790e2eb0914180ae60d557556e8f11.apk" }" target="_blank">下载</a>
                </div>
            </div>
        </div>
    </div>
    <div class="main" style="padding-bottom:100px;">
        <div class="container">
            <div class="repeat">
                <div class="panel panel-default">
                    <div class="panel-body book-icon">
                        <c:forEach items="${course.icon }" var="icon">
                            <img class="img-responsive" src="http://res.kidbridge.org/${icon }">
                        </c:forEach>
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">故事梗概</div>
                    <div class="panel-body book-outline">
                        ${empty course.outline ? "暂无内容" : course.outline }
                    </div>
                </div>
                <div class="panel panel-default">
                    <div class="panel-heading">听我跟读</div>
                    <div class="panel-body book-repeat">
                        <div class="user-head pull-left">
                            <img src="http://res.kidbridge.org/${user.head }">
                        </div>
                        <div class="audio pull-left">
                            <div class="tip pull-left">
                                跟读音频
                            </div>
                            <div class="time pull-right">

                            </div>
                        </div>
                        <div class="player pull-left" status="0">
                            <i class="fa fa-play-circle-o" aria-hidden="true"></i>
                        </div>
                    </div>
                </div>
                <div id="jplayer"></div>
            </div>
        </div>
    </div>
    <div class="qrcode" style="position: fixed;bottom:0;width: 100%;">
        <div class="detail" style="padding: 10px 0;">
            <div class="img" style="float: left;margin-left: 15px;">
                <img style="height: 80px;" class="img-responsive center-block" src="data:image/jpeg;base64,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">
            </div>
            <div class="des" style="overflow: hidden;text-align: left;padding: 0 10px;color: white;height: 80px;">
                与美国小朋友共读英文原版绘本，每日打卡、快乐朗读 ~ <br><br>
                更多精彩内容请扫描二维码查看。
            </div>
        </div>
    </div>

    <script src="//cdn.bootcss.com/jquery/1.12.4/jquery.min.js"></script>
    <script src="//cdn.bootcss.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="/resources/js/plugins/slick/slick.min.js"></script>
    <script src="//cdn.bootcss.com/jplayer/2.9.2/jplayer/jquery.jplayer.min.js"></script>
    <script>
        var time = 0;
        var index = 0;
        var audio = ${repeat };
        for(var i = 0;i<audio.length;i++){
            time += audio[i].audio.time;
        }
        $(".time").text(parseInt(time/1000) + " ''");
        $('.book-icon').slick({
            arrows: false,
            autoplay: true,
            centerMode: ${book.icon.size() > 1 },
            centerPadding: '10%',
            slidesToShow: 1
        });
        var player = $("#jplayer").jPlayer({
            supplied: "mp3",
            ended: function () {
                index++;
                if(index>=audio.length){
                    index = 0;
                    $(".player").attr("status","0");
                    $(".player i").attr("class","fa fa-play-circle-o");
                    player.jPlayer("setMedia", {
                        mp3: 'http://res.kidbridge.org/'+audio[index].audio.source+''
                    });
                    return;
                }
                player.jPlayer("setMedia", {
                    mp3: 'http://res.kidbridge.org/'+audio[index].audio.source+''
                });
                player.jPlayer("play");
            }
        });
        $(".book-repeat").on("click",".player[status='0']",function () {
            player.jPlayer("setMedia", {
                mp3: 'http://res.kidbridge.org/'+audio[index].audio.source+''
            });
            $(".player").attr("status","1");
            $(".player i").attr("class","fa fa-stop-circle-o");
            player.jPlayer("play");
        });
        $(".book-repeat").on("click",".player[status='1']",function () {
            index = 0;
            $(".player").attr("status","0");
            $(".player i").attr("class","fa fa-play-circle-o");
            player.jPlayer("stop");
        });

        /*$(".icon-close").on("click",function () {
            $(this).parents(".app-download").hide();
        });*/
    </script>
</body>
</html>
