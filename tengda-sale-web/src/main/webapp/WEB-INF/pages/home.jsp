<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- 上述3个meta标签*必须*放在最前面，任何其他内容都*必须*跟随其后！ -->
    <title>配件管理</title>

    <!-- 新 Bootstrap 核心 CSS 文件 -->
    <link rel="stylesheet" href="//cdn.bootcss.com/bootstrap/3.3.5/css/bootstrap.min.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/toastr.js/latest/css/toastr.css">
    <link rel="stylesheet" href="//cdn.bootcss.com/simplePagination.js/1.6/simplePagination.min.css">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
    <script src="//cdn.bootcss.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="//cdn.bootcss.com/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
    <style>
        ::-webkit-scrollbar {
            width: 6px;
            height: 6px;
            background-color: #F5F5F5;
        }

        ::-webkit-scrollbar-track {
            -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, 0.3);
            border-radius: 10px;
            background-color: #F5F5F5;
        }

        ::-webkit-scrollbar-thumb {
            border-radius: 0px;
            -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, .3);
            background-color: #555;
        }

        .l-head {
            position: absolute;
            top: 0;
            left: 0;
            width: 100%;
            height: 70px;
            z-index: 9;
            padding: 0 530px 0 196px;
            background: #fff;
            border-bottom: 1px solid #d9d9d9;
            color: #6c757e;
        }

        .jd-navHeader {
            height: 100%;
            font-size: 0;
        }

        .jd-navHeader-item {
            position: relative;
            float: left;
            display: table;
            height: 100%;
            padding: 0 10px;
            margin: 0 15px;
            cursor: pointer;
            -webkit-user-select: none;
            -moz-user-select: none;
            -ms-user-select: none;
            user-select: none;
        }

        .jd-navHeader-text {
            display: table-cell;
            max-width: 250px;
            overflow: hidden;
            white-space: nowrap;
            text-overflow: ellipsis;
            vertical-align: middle;
            text-align: center;
            font-size: 16px;
            font-weight: 700;
            letter-spacing: 1px;
        }

        .jd-navHeader-item.isActive {
            color: #4ba2fa;
            border-bottom: 5px solid #4ba2fa;
        }

        .functionInfo{
            font-size: 12px;
            display: block;
            background: none repeat scroll 0 0;
            background-color: #555555;
            border-radius:4px 4px 4px 4px;
            box-shadow: rgba(0, 0, 0, 0.25) 0px 0px 10px inset;
            clear: both;
            font-family: 'Consolas', 'Courier', 'Monaco', monospace;
            color: #fff;
            /*background-color: #f8f8f8;*/
            margin: 5px 0px;
            overflow: auto;
            padding: 10px;
            white-space: pre;
        }

        @media (min-width: 1200px) {
            .container {
                width: 1450px;
            }
        }
    </style>
</head>
<body>

<header class="l-head">
    <ul class="jd-navHeader">
        <li class="jd-navHeader-item sale-parts isActive"><span class="jd-navHeader-text">配件管理</span></li>
        <li class="jd-navHeader-item sale-record"><span class="jd-navHeader-text">配件进/出货管理</span></li>
        <li class="jd-navHeader-item sale-report"><span class="jd-navHeader-text">业务报表</span></li>
    </ul>
</header>

<div class="container" style="margin-top: 70px;">
</div>

<div class="modal fade" id="loadingModal" backdrop="static" keyboard="false">
    　　
    <div style="width: 200px;height:20px; z-index: 20000; position: absolute; text-align: center; left: 50%; top: 50%;margin-left:-100px;margin-top:-10px">
        　　　　
        <div class="progress progress-striped active">
            <div class="progress-bar progress-bar-success" role="progressbar"
                 aria-valuenow="60" aria-valuemin="0" aria-valuemax="100"
                 style="width: 80%;">
                <span class="sr-only">处理中...</span>
            </div>
        </div>
        　　
    </div>
</div>

<!-- jQuery文件。务必在bootstrap.min.js 之前引入 -->
<script src="//cdn.bootcss.com/jquery/1.11.3/jquery.min.js"></script>

<!-- 最新的 Bootstrap 核心 JavaScript 文件 -->
<script src="//cdn.bootcss.com/bootstrap/3.3.5/js/bootstrap.min.js"></script>
<script src="//cdn.bootcss.com/toastr.js/latest/toastr.min.js"></script>
<script src="//cdn.bootcss.com/simplePagination.js/1.6/jquery.simplePagination.min.js"></script>

<!-- 通用视图渲染 -->
<script src="../common/common.js"></script>

<script>
    function saleParts() {
        renderModule({
            url: "/sale_parts/list",
            params: {pageSize: 10, pageNo: 1},
            title: "配件管理",
            headers: ["配件编号", "配件名称", "配件价格（元）", "配件库存数量（个）", "创建时间"],
            attrNames: ["partsCode", "partsName", "currentPrice", "totalNum", "createTimeText"]
        }, {
            url: "/sale_parts/delete",
            params: {}
        }, {
            url: "/sale_parts/modify",
            attrNames: ["partsName", "currentPrice", "totalNum"]
        }, {
            url: "/sale_parts/add",
            headers: ["配件编号", "配件名称", "配件价格（元）", "配件库存数量（个）"],
            attrNames: ["partsCode", "partsName", "currentPrice", "totalNum"]
        });
        document.title = $(".isActive").text();
    }

    saleParts();
    $(".sale-parts").click(function () {
        $(".jd-navHeader-item").removeClass("isActive");
        $(this).addClass("isActive");
        saleParts();
    });

    $(".sale-record").click(function () {
        $(".jd-navHeader-item").removeClass("isActive");
        $(this).addClass("isActive");
        renderModule({
            url: "/sale_record/list",
            params: {pageSize: 10, pageNo: 1},
            title: "配件进/出货管理",
            headers: ["进/出货类型", "配件编号", "配件名称", "配件数量（个）", "配件价格（元）", "收款金额（元）", "销售对象", "创建时间", "更新时间"],
            attrNames: ["typeText", "partsCode", "partsName", "partsNum", "partsPrice", "orderAmount", "saleObject", "createTimeText", "updateTimeText"]
        }, {
            url: "/sale_record/delete",
            params: {}
        }, {
            url: "/sale_record/modify",
            attrNames: ["partsNum", "partsPrice", "orderAmount", "saleObject"]
        }, {
            url: "/sale_record/add",
            headers: ["进/出货类型", "配件编号", "配件名称", "配件数量（个）", "配件价格（元）", "收款金额（元）", "销售对象"],
            attrNames: ["type", "partsCode", "partsName", "partsNum", "partsPrice", "orderAmount", "saleObject"]
        });
        document.title = $(".isActive").text();
    });

    function reportCallback(result) {
        $(".data-row").each(function () {
            var item_id = $(this).attr("item-id");
            $(this).find(".opreate-td").append(" <a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-sm btn-danger btn-execute-sql\" item-id=\"" + item_id + "\"> 执行</a>");
        });

        $(".btn-execute-sql").click(function () {
            var item_id = $(this).attr("item-id");
            renderSimpleList({
                url: "/sale_report/execute/" + item_id,
                params: {pageSize: 10, pageNo: 1}
            });
        });

        $(".container").prepend("<div class='functionInfo'>系统时间函数说明=》" + result.functionInfo + "</div>");
        $(".container").find("th").eq(0).css("min-width","200px");
    }

    $(".sale-report").click(function () {
        $(".jd-navHeader-item").removeClass("isActive");
        $(this).addClass("isActive");
        renderModule({
            url: "/sale_report/list",
            params: {pageSize: 10, pageNo: 1},
            title: "业务报表",
            headers: ["报表名称", "报表SQL"],
            attrNames: ["name", "querySql"],
            userCallback: reportCallback
        }, {
            url: "/sale_report/delete",
            params: {}
        }, {
            url: "/sale_report/modify",
            attrNames: ["name", "querySql"]
        }, {
            url: "/sale_report/add",
            headers: ["报表名称", "报表SQL"],
            attrNames: ["name", "querySql"]
        });
        document.title = $(".isActive").text();
    });

</script>

</body>
</html>