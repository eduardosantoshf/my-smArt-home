


var EndSer='http://localhost:8080/';
var virtualKey = localStorage.getItem("zlsvktg"); // when user sign-in for the first time, server sends a random string with the virtual key for further uses. The key should work only for this user.
var username = localStorage.getItem("username");

function initLogin(){
    // initial verification: combines username with the virtual key provided by server to check if user is already authenticated
    const url=EndSer+'user/login';
    if(username!=null){
        $.ajax(url,{
            type:'POST',
            data:{username: username, pwd:virtualKey},
            success: function(data, status, xhr){
                var obj=JSON.parse(data);
                if(obj.status==true){ // se já estiver autenticado
                    window.location.href="Profile.html";
                    return true;
                }
            }
        });
    }
    return false;
}

function initRegister(){
    // initial verification: combines username with the virtual key provided by server to check if user is already authenticated
    const url=EndSer+'user/login';
    if(username!=null){
        $.ajax(url,{
            type:'POST',
            data:{username: username, pwd:virtualKey},
            success: function(data, status, xhr){
                var obj=JSON.parse(data);
                if(obj.status==true){ // se já estiver autenticado
                    window.location.href="Profile.html";
                    return true;
                }
            }
        });
    }
    return false;
}

function initDashboard(){
    const url=EnsSer+'user/login';
    if(username!=null){
        $.ajax(url,{
            type:'POST',
            data:{username: username, pwd:virtualKey},
            success: function(data, status, xhr){
                var obj=JSON.parse(data);
                if(obj.status==false){ // se já estiver autenticado
                    window.location.href="Login.html";
                }
            }
        });
    }else{
        window.location.href=""
    }
}

/* Set the width of the sidebar to 250px and the left margin of the page content to 250px */
function openNav() {
    document.getElementById("mySidebar").style.width = "500px";
    document.getElementById("main").style.marginLeft = "500px";
  }
  
/* Set the width of the sidebar to 0 and the left margin of the page content to 0 */
function closeNav() {
    document.getElementById("mySidebar").style.width = "0";
    document.getElementById("main").style.marginLeft = "0";
}


if(!window.location.href.includes("Login.html") && !window.location.href.includes("Register.html")){
    if(!localStorage.getItem("username") || !localStorage.getItem("token"))
        window.location.href="Login.html";
        
    $.ajax(EndSer+"user/login",{
        type:'GET',
        data:{username:localStorage.getItem("username"), pwd:localStorage.getItem("token")},
        success: function(data, status, xhr){
            //console.log(data)
            var obj=JSON.parse(data);
            if(obj.status==false){ // se não estiver autenticado
                window.location.href="Login.html";
            }
        }
    });
}


setInterval(function(){
    username=localStorage.getItem("username");
    $.ajax(EndSer+"notifications/getAll/"+username,{
        type:'GET',
        success: function(data, status, xhr){
            var obj = JSON.parse(data)
            obj.notificacoes.forEach(not => {
                $("#mynots").html($("#mynots").html() + '<div>Device: '+not.deviceId+'; Information: '+not.value+'; Date: '+not.date+'</div>');
            });
            //console.log(obj)
        }
    });
}, 3000);