
// excepcionalmente esta função fica neste ficheiro porque não temos acesso à função getDevices() a partir do ficheiro basic.js 
function initDevices(){
    initDashboard(); // presente no documento basic.js
    getDevices();
}

function getDevices(){
    const url=EndSer + 'user/'+localStorage.getItem("username"); // EndSer é uma variavel que vem do ficheiro basic.js      como basic.js é chamado antes de login.js (no ficheiro Login.html, então, as variaveis e funções de basic.js passam para login.js)
    username=localStorage.getItem("username");
    $.ajax(url,{
        type:'GET',
        data:{username:username},
        success: function(data, status, xhr){
            var obj=data;
            console.log(obj);
            user_home=obj.homes_id;
            if(user_home.length==0){
                $.ajax(EndSer+"user/smarthome",{
                    type:'POST',
                    data:{username:username},
                    success: function(data, status, xhr){
                        getDevices();
                    }
                });
            }
            // if there is any SmartHome
            $.ajax(EndSer+"device/alldevices/"+username,{
                type:'GET',
                success: function(data2, status, xhr){
                    var obj=JSON.parse(data2);
                    if(obj.devices.length == 0){
                        $.ajax(EndSer+"device/hardcheck/"+username,{
                            type:'GET',
                            success: function(data3, status, xhr){
                                console.log(data3)
                            }
                        });
                    }
                }
            });

            /*if(obj.status==true){ // se já estiver autenticado
                var ihtml = '';
                obj.devices.forEach(device => {
                    ihtml+='<tr> <td>'+device.id+'</td> <td>'+device.name+'</td> <td>tipo</td> <td>estado (online/offline)</td> <td>-</td> </tr>';
                });
                document.getElementById("listDevices").innerHTML=ihtml;
            }else{
                alert(obj.reason)
            }*/
        }
    });
}