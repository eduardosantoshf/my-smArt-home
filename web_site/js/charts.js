
var devices = [];
function getDevices(){
    devices=[];
    const url=EndSer + 'user/'+localStorage.getItem("username"); // EndSer é uma variavel que vem do ficheiro basic.js      como basic.js é chamado antes de login.js (no ficheiro Login.html, então, as variaveis e funções de basic.js passam para login.js)
    username=localStorage.getItem("username");
    $.ajax(url,{
        type:'GET',
        data:{username:username},
        success: function(data, status, xhr){
            var obj=data;
            console.log(obj);
            user_home=obj.homes_id;
            localStorage.setItem("user_home", user_home);
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
                    console.log(obj2)
                    if(obj2.devices.length == 0){
                        hardcheck();
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
                                    if(device[2].type!=undefined && device[2].type!="None"){
                                        type=device[2].type
                                    }

                                    var active_since="";
                                    if(device[3].active_since!=undefined && device[3].active_since!="None"){
                                        active_since=device[3].active_since
                                    }

                                    if(device[1].status!=undefined && device[1].status!="None" && device[2].type!=undefined && device[2].type!="None" && device[3].active_since!=undefined && device[3].active_since!="None")
                                        addDevice(id, status, type, active_since)
                                }
                            });
                        });
                    }
                }
            });
        }
    });
}

function addDevice(id, status, type, active_since){
    devices.push({id: id, status:status, type:type, active_since:active_since});

    $.ajax(EndSer+"device/graphs/"+id,{
        type:'GET',
        success: function(data4, status, xhr){
            console.log(data4)
        }
    });

}

function drawChart(id, data, title) {
    var data = google.visualization.arrayToDataTable([
      ['Task', 'Hours per Day'],
      ['Work',     11],
      ['Eat',      2],
      ['Commute',  2],
      ['Watch TV', 2],
      ['Sleep',    7]
    ]);

    var options = {
      title: 'Daily Values'
    };

    var chart = new google.visualization.PieChart(document.getElementById(id));
    chart.draw(data, options);
}