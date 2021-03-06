<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>首页</title>
    <link href="${request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${request.contextPath}/static/css/flat-ui.css" rel="stylesheet">
    <script src="${request.contextPath}/static/js/jquery.min.js"></script>
    <script src="${request.contextPath}/static/js/flat-ui.min.js"></script>
    <script src="${request.contextPath}/static/js/application.js"></script>
</head>
<body>
<nav class="navbar navbar-inverse" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-5">
            <span class="sr-only">Toggle navigation</span>
        </button>
        <a class="navbar-brand" href="${request.contextPath}">首页</a>
    </div>

    <div class="collapse navbar-collapse" id="navbar-collapse-5">
        <ul class="nav navbar-nav">
            <li><a href="#">Messages<span class="navbar-unread">1</span></a></li>
            <li class="active"><a href="#">About Us</a></li>
            <li><a href="#">Clients</a></li>
        </ul>
        <form class="navbar-form navbar-left" action="#" role="search">
            <div class="form-group">
                <div class="input-group">
                    <input class="form-control" id="navbarInput-01" type="search" placeholder="Search">
                    <span class="input-group-btn">
                  <button type="submit" class="btn"><span class="fui-search"></span></button>
                </span>
                </div>
            </div>
        </form>
        <ul class="nav navbar-nav navbar-right">
            <li class="dropdown">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">MonsterCritic <b class="caret"></b></a>
                <ul class="dropdown-menu">
                    <li><a href="#">Action</a></li>
                    <li><a href="#">Another action</a></li>
                    <li><a href="#">Something else here</a></li>
                    <li class="divider"></li>
                    <li><a href="#">Separated link</a></li>
                </ul>
            </li>
            <li><a href="#"><span class="visible-sm visible-xs">Settings<span class="fui-gear"></span></span><span class="visible-md visible-lg"><span class="fui-gear"></span></span></a></li>
        </ul>
    </div><!-- /.navbar-collapse -->
</nav>

    <h1> hello </h1>
    <h3>
        <a href="${base}/user/toLogin">登陆</a>
    </h3>
    <h6>
        <a href="${base}/item/toList">商品列表</a>
    </h6>
    <h6>
        <a href="${base}/order/list">订单列表</a>
    </h6>
    <h6>
        <a href="javascript:0" onclick="toDownloadUrl()">本月支付宝对账单查询</a>
    </h6>
    <p>
        12345
    </p>

    <script>
        function toDownloadUrl() {
            $.ajax({
                url: "${base}/pay/alipayDataBillDownLoad",
                type: "POST",
                data: "",
                dataType: "json",
                success: function(data){
                    if(data.code === 0){
                        alert(data.msg);
                    }else{
                        console.log("对账单请求成功");
                        window.location.href = data.data;
                    }
                }
            });
        }
    </script>
</body>
</html>