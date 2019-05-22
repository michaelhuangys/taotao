var TT = TAOTAO = {
	checkLogin : function(){
		var _ticket = $.cookie("TT_TOKEN");
		if(!_ticket){
			return ;
		}
		$.ajax({
			url : "http://localhost:8084/user/token/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					var username = data.data.username;
					
					var html = username + "，欢迎来到淘淘！<a href=\"#\" class=\"link-logout\" onclick=\"logout()\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});
	}
}

function logout(){
	var _ticket = $.cookie("TT_TOKEN");
	console.log("http://localhost:8084/user/token/logout/"+_ticket)
	$.ajax({
		type:'get',
		url:"http://localhost:8084/user/token/logout/"+_ticket,
		contentType:"application/json",
		dataType: 'jsonp',
		 complete: function(xhr) {        	
             if (xhr.status == 200) 
             {
            	 history.go(0); 
             }else{
            	 alert("系统异常");
             }
         }
	})
	
}
$(function(){
	// 查看是否已经登录，如果已经登录查询登录信息
	TT.checkLogin();
});