<#assign base = request.contextPath />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>商品列表</title>
    <link href="${request.contextPath}/static/css/bootstrap.min.css" rel="stylesheet">
    <link href="${request.contextPath}/static/css/flat-ui.css" rel="stylesheet">
    <script src="${request.contextPath}/static/js/jquery.min.js"></script>
    <script src="${request.contextPath}/static/js/flat-ui.min.js"></script>
    <script src="${request.contextPath}/static/js/application.js"></script>
    <script>
        function toBuy(obj) {
            console.log('success')
            var item = $(obj).parent().parent().find("td");
            console.log(item)
            var id = item.eq(0).text();
            var name = item.eq(1).text();
            var amount = item.eq(2).text();
            console.log(id)
            console.log(name)
            console.log(amount)
            $.ajax({
                url: "${base}/order/save",
                type: "post",
                data: {"id":id,"name":name,"amount":amount},
                dataType: "json",
                success: function(data){
                    if(data.code === 0){
                        alert(data.msg);
                    }else{
                        console.log("下单成功");
                        window.location.href = "${base}/order/list";
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
                <td><b>编号</b></td>
                <td><b>名称</b></td>
                <td><b>价格</b></td>
                <td><b>操作</b></td>
            </tr>
            <#list ItemList as list>
                <tr>
                    <td>${list.id}</td>
                    <td>${list.name}</td>
                    <td>${list.price}</td>
                    <td>
                        <input type="button" value="下单购买" onclick="toBuy(this)"/>
                    </td>
                </tr>
            </#list>
        </table>


    </h3>
</body>

</html>