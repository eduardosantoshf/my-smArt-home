


var EndSer='http://localhost/';
var virtualKey = localStorage.getItem("zlsvktg"); // when user sign-in for the first time, server sends a random string with the virtual key for further uses. The key should work only for this user.
var username = localStorage.getItem("username");

function initLogin(){
    // initial verification: combines username with the virtual key provided by server to check if user is already authenticated
    const url=EndSer+'login';
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
    const url=EndSer+'login';
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
    const url=EnsSer+'login';
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

