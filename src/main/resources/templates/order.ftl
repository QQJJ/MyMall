<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>订单列表</title>
    <link href="${request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${request.contextPath}/static/css/flat-ui.css" rel="stylesheet">
    <script src="${request.contextPath}/static/js/jquery.min.js"></script>
    <script src="${request.contextPath}/static/js/flat-ui.min.js"></script>
    <script src="${request.contextPath}/static/js/application.js"></script>
    <script>
        function toPay(obj) {
            var item = $(obj).parent().parent().find("td");
            var id = item.eq(0).text();
            window.location.href = "${base}/pay/pay/" + id;
        }

        function toWapPay(obj) {
            var item = $(obj).parent().parent().find("td");
            var id = item.eq(0).text();
            window.location.href = "${base}/pay/wapPay/" + id;
        }

        function toOrderDetail(Obj){
            alert("查看订单详情")
        }

        function toRefund(Obj){
            var item = $(Obj).parent().parent().find("td");
            var id = item.eq(0).text();
            $.ajax({
                url: "${base}/pay/refund/"+id,
                type: "POST",
                data: "",
                dataType: "json",
                success: function(data){
                    if(data.code === 0){
                        alert(data.msg);
                    }else{
                        console.log("退款成功");
                        window.location.reload(true);
                    }
                }
            });
        }

    </script>
</head>
<body>
<nav class="navbar navbar-inverse" role="navigation">
    <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#navbar-collapse-5">
            <span class="sr-only">Toggle navigation</span>
        </button>
        <a class="navbar-brand" href="${request.contextPath}">商品列表</a>
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

    <h3>
        <table style="text-align:center;FONT-SIZE: 11pt; WIDTH: 600px; FONT-FAMILY: 宋体,serif; BORDER-COLLAPSE: collapse" borderColor=#3399ff cellSpacing=0 cellPadding=0 align=center border=1>
            <tr>
                <td><b>订单id</b></td>
                <td><b>订单号</b></td>
                <td><b>金额</b></td>
                <td><b>创建人</b></td>
                <td><b>操作</b></td>
            </tr>
            <#list OrderList as order>
                <tr>
                    <td>${order.id}</td>
                    <td>${order.orderNo}</td>
                    <td>${order.amount}</td>
                    <td>${order.createrName}</td>
                    <#if order.status?? && order.status == 1>
                        <td><button type="button" class="btn btn-success" onclick="toOrderDetail(this)">已支付,查看详情</button></td>
                    </#if>
                    <#if order.status?? && order.status == 0>
                        <td>
                            <button type="button" class="btn btn-primary" onclick="toPay(this)">未支付,WEB支付</button>
                            <button type="button" class="btn btn-primary" onclick="toWapPay(this)">未支付,WAP支付</button>
                        </td>
                    </#if>
                    <#if order.status?? && order.status == 2>
                        <td><button type="button" class="btn btn-success" onclick="toRefund(this)">已支付,点击退款</button></td>
                    </#if>
                    <#if order.status?? && order.status == 3>
                        <td><button type="button" class="btn btn-success"">已支付不可退款</button></td>
                    </#if>
                    <#if order.status?? && order.status == 4>
                        <td><button type="button" class="btn btn-primary"">已取消</button></td>
                    </#if>
                     <#if order.status?? && order.status == 5>
                        <td><button type="button" class="btn btn-primary"">已退款</button></td>
                     </#if>
                </tr>
            </#list>
        </table>


    </h3>
</body>

</html>