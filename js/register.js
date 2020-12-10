
function register(){
    // initial verification: combines username with the virtual key provided by server to check if user is already authenticated
    var username=document.getElementById("usernameTxt").value;
    var email=document.getElementById("emailTxt").value;
    var password=document.getElementById("passwordTxt").value;

    if(username=="" || email=="" || password==""){
        alert("You need to fill all fields!");
        return true;
    }

    const url=EndSer + 'register'; // EndSer é uma variavel que vem do ficheiro basic.js      como basic.js é chamado antes de login.js (no ficheiro Login.html, então, as variaveis e funções de basic.js passam para login.js)
    if(username!=null){
        $.ajax(url,{
            type:'POST',
            data:{username:username, pwd:password},
            success: function(data, status, xhr){
                var obj=JSON.parse(data);
                if(obj.status==true){ // se já estiver autenticado
                    window.location.href="Profile.html";
                    return true;
                }else{
                    alert(obj.reason)
                }
            }
        });
    }
    return false;
}