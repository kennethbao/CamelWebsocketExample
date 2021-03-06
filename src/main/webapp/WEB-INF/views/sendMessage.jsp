<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title></title>
    <!--
	<link rel="stylesheet" href="/css/style.css"/>
    -->
    <script type="text/javascript" src="http://cdn.bootcss.com/jquery/3.1.0/jquery.min.js"></script>
    <script type="text/javascript" src="http://cdn.bootcss.com/sockjs-client/1.1.1/sockjs.js"></script>
    <script type="text/javascript">
        var websocket = null;
        var url1 = "ws://localhost:8090/websocket/websocketTest";
        var url2 = "ws://localhost:8090/echo";
        if ('WebSocket' in window) {
            websocket = new WebSocket(url1);
        }
        else if ('MozWebSocket' in window) {
            websocket = new MozWebSocket(url1);
        }
        else {
            websocket = new SockJS(url1);
        }
        websocket.onopen = onOpen;
        websocket.onmessage = onMessage;
        websocket.onerror = onError;
        websocket.onclose = onClose;

        function onOpen(openEvt) {
            alert(openEvt.Data);
        }

        function onMessage(evt) {
            alert(evt.data);
        }
        function onError() {}
        function onClose() {}

        function doSendUser() {
            if (websocket.readyState == websocket.OPEN) {
                var msg = document.getElementById("inputMsg").value;
                websocket.send("#anyone#"+msg);//调用后台handleTextMessage方法
                alert("发送成功!");
            } else {
                alert("连接失败!");
            }
        }


        function doSendUsers() {
            if (websocket.readyState == websocket.OPEN) {
                var msg = document.getElementById("inputMsg").value;
                websocket.send("#everyone#"+msg);//调用后台handleTextMessage方法
                alert("发送成功!");
            } else {
                alert("连接失败!");
            }
        }

        function doSendCamel(){
           // websocket = new WebSocket("http://localhost:8090//demo/1");

        }


        window.close=function()
        {
            websocket.onclose();
        }
    </script>

</head>
<body>

请输入：<textarea rows="5" cols="10" id="inputMsg" name="inputMsg"></textarea>
<button onclick="doSendUser();">发送</button>
<button onclick="doSendUsers();">群发</button>
<button onclick="doSendCamel()";>camel</button>
</body>
</html>
