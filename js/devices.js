
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
            var user_devices=[]
            // if there is any SmartHome
            $.ajax(EndSer+"device/alldevices/"+username,{
                type:'GET',
                success: function(data2, status, xhr){
                    var obj2=JSON.parse(data2);
                    user_devices=obj2.devices;
                    if(obj2.devices.length == 0){
                        $.ajax(EndSer+"device/hardcheck/"+username,{
                            type:'GET',
                            success: function(data3, status, xhr){
                                var obj3=JSON.parse(data3);
                                user_devices=obj3.devices;
                                fullFill(user_devices);

                                user_devices.forEach(device => {
                                    $.ajax(EndSer+"device/post",{
                                        type:'POST',
                                        data:{id_home:user_home[0], device_id: parseInt(device.id_)},
                                        success: function(data4, status, xhr){
                                            
                                        }
                                    });
                                });
                                
                            }
                        });
                    }else{
                        user_devices.forEach(device => {
                            $.ajax(EndSer+"device/"+device.id,{
                                type:'GET',
                                success: function(data4, status, xhr){
                                    var obj = JSON.parse(data4);
                                    var device = obj.device;
                                    var id=device[0].id;
                                    var status="";
                                    if(device[1].status!=undefined && device[1].status!="None"){
                                        status=device[1].status
                                    }
                                    var type="";
                                    if(device[2].type!=undefined && device[1].type!="None"){
                                        type=device[2].type
                                    }

                                    if(device[1].status!=undefined && device[1].status!="None" && device[2].type!=undefined && device[1].type!="None")
                                        addDevice(id, status, type)
                                    
                                }
                            });
                        });
                        
                    }
                }
            });
        }
    });
}

function fullFill(user_devices){
    if(user_devices.length>0){
        var ihtml='';
        user_devices.forEach(device => {
            ihtml+='<tr> <td>'+parseInt(device.id_)+'</td> <td>-</td> <td>'+device.type_+'</td> <td>'+device.status+'</td> <td>-</td> </tr>';
            document.getElementById("listDevices").innerHTML=ihtml;
        });
    }
}

function addDevice(id, status, type){
    var ihtml='<tr> <td>'+id+'</td> <td>-</td> <td>'+type+'</td> <td>'+status+'</td> <td>-</td> </tr>';
    document.getElementById("listDevices").innerHTML=document.getElementById("listDevices").innerHTML + ihtml;
}