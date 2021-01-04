
// excepcionalmente esta função fica neste ficheiro porque não temos acesso à função getDevices() a partir do ficheiro basic.js 
function initDevices(){
    initDashboard(); // presente no documento basic.js
    getDevices();
}

function getDevices(){
    const url=EndSer + 'devices'; // EndSer é uma variavel que vem do ficheiro basic.js      como basic.js é chamado antes de login.js (no ficheiro Login.html, então, as variaveis e funções de basic.js passam para login.js)
    if(username!=null){
        $.ajax(url,{
            type:'POST',
            data:{username:username},
            success: function(data, status, xhr){
                var obj=JSON.parse(data);
                if(obj.status==true){ // se já estiver autenticado
                    var ihtml = '';
                    obj.devices.forEach(device => {
                        ihtml+='<tr> <td>'+device.id+'</td> <td>'+device.name+'</td> <td>tipo</td> <td>estado (online/offline)</td> <td>-</td> </tr>';
                    });
                    document.getElementById("listDevices").innerHTML=ihtml;
                }else{
                    alert(obj.reason)
                }
            }
        });
    }
}