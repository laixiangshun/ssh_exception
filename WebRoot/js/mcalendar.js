/*
*  Editor by fgg on 2017-03-07
*/
//鼠标经过日期样式反馈
function mouseIn(e){
    $(e).addClass("event-over");
}
function mouseOut(e){
    $(e).removeClass("event-over");
}
//日历表格的每行
function drawtablerow(d,markdate,year,month){
	//设置有效时间
    var effectiveDay = 30;
    //有效日期
    var eftdate = new Date();
    var currentdate = new Date();
    eftdate.setDate(eftdate.getDate()+effectiveDay);
    //正在渲染的td中的日期
	var thisdate = new Date(year+"-"+month+"-"+d);
    var markClass = "";//预约类名
    var onclick = "onclick='wrongClick()'";//点击事件
    var mousemove = "";//鼠标移动事件
    var tdcolor = "blueTr";//判断是否为当前日期
	//通过传入的日期数组判断在哪些天需要标记为有预约
	for(var mark = 0;markdate && mark<markdate.length;mark++){
        if( d == markdate[mark]){
            markClass=" haveMark";
            break;
        }
    }
	//有效日期
    if(thisdate < eftdate && thisdate.getDay() != 1){
    	onclick = "onclick='calendarClick(this)'";
    	mousemove = "onmouseover='mouseIn(this)' onmouseout='mouseOut(this)'";
    }
    //当前日期
    if(thisdate.getDate() == currentdate.getDate()){
    	if(thisdate.getMonth() == currentdate.getMonth()){
    		if(thisdate.getFullYear() == currentdate.getFullYear()){
    			tdcolor = "redTr";
    		}
    	}
    }
    return '<td class="'+tdcolor+markClass+'" '+onclick+ mousemove+' value="'+ d + '">'+ d +'</td>';
}

(function(){
var calendar = function(calendarId,markdate){
    //日历对象不存在，返回日历
    if(!(this instanceof calendar)){
        return new calendar();
    }
    //获取当前日历对象
    var c =this;
    //获取引用日历的节点id，这是外面提供的要放哪里的id
    var calendarId = document.getElementById(calendarId);
    //动态加载样式
    function loadStyles(str){
        loadStyles.mark = 'load';
        var style = document.createElement("style");
        style.type = "text/css";
        try{
            style.innerHTML = str;
        }catch(ex){
            style.styleSheet.cssText = str;
        }
        var head = document.getElementsByTagName('head')[0];
        head.appendChild(style); 
    }
    //生成节点的函数
    c.buildPart = function(json){
        var oPart = document.createElement(json.node);
        if(json.id){oPart.setAttribute('id',json.id);}
        if(json.className){oPart.className = json.className;}
        if(json.innerHTML){oPart.innerHTML = json.innerHTML;}
        if(json.href){oPart.setAttribute('href',json.href);}
        if(json.appendToBox){
            json.appendToBox.appendChild(oPart);
        }
        return oPart;
    }
    //全局初始化日期表格tr字符串
    var CalendarTr;
    
    //获取当前的年月，用于初始化显示页面
    var thisyear =  new Date().getFullYear();
    var thismonth =  new Date().getMonth()+1;
    //生成日历的函数
    c.getcalendar = function(year,month){
        markdate = getReservationData(year + "-" + String(month<10? '0'+month : month)); 
    	//传出当前年月
    	clickDate = String(year) + "-" +String(month<10? '0'+month : month);
    	$("#selectMonth").val(clickDate);
        var last = new Date(year,month-1,0);//获取上个月份的时间对象
        var lastdate = last.getDate();//获取上个月的最大日期
        var lastday = last.getDay();//获取上个月最大日期的星期几
        var months = new Date(year,month,0);
        var Maxdate = months.getDate();//获取这个月最大的日期
        //星期表头
        var str = '<tr><th>星期日</th><th>星期一</th><th>星期二</th><th>星期三</th><th>星期四</th><th>星期五</th><th>星期六</th></tr>';
        //获取上个月在这个月份中存在的最后日期
        var remainlastdate = lastdate - lastday;
        var t = 1;
        //日历的日期
        //日历第一排
        str += '<tr>';
        for(var g=0;g<7;g++){
        //星期是从0开始到6结束的，小于等于上个月最大星期的日期则是上个月的日期
            if(g <= lastday){
                str +='<td class="Tr">'+ remainlastdate++ +'</td>';
            }else{
            	str += drawtablerow(t++,markdate,year,month);
            }
        }
        str += '</tr>';
        //日历的其他排，这个月从哪里开始
        var remainlastday = 6 - lastday + 1;
        var nextMonthday = 1;//下一个月份的开始日期
        //这里i最大值为5是为了兼容所有的月份，有的月份可能跨越6个tr
        for(var i=1;i<6;i++){
            str += '<tr>';
            //渲染时通过传入的日期数组判断在哪些天需要标记为有预约
            for(var j=0;j<7;j++){
                if(remainlastday <= Maxdate){
                	str += drawtablerow(remainlastday++,markdate,year,month);
                }else{
                    str +='<td class="Tr">'+ nextMonthday++ +'</td>';
                }
            }
            str += '</tr>';
        }
        return str;
    }
    //初始化内部构造函数
    c.init = function(){
        //生成日历的表格tr
        CalendarTr = c.getcalendar(thisyear,thismonth);
        if(loadStyles.mark != 'load'){
        //.haveMark类用于标记有预约的日期
        loadStyles(".calendarDiv {width:461px;text-align:center;}\.event-over{opacity: 0.6;}\
            .calendar-table{width:460px;height:300px;}\#headId{width: 460px;margin: 0;}\
            .haveMark1{background-image: url('${path}/images/calenderpic.png');background-repeat: no-repeat;background-position: 90% 10%;}\
            .box{margin-left:15px;margin-right:10px;text-align:center;width:420px;height:42px;font-weight:bold;font-size:21px;}\
            .box-dec{border:none;text-align:left;cursor:pointer;font-weight:bold;float:left;}\
            .box-add{border:none;text-align:right;cursor:pointer;font-weight:bold;float:right;}\
            .Tr{text-align:center;}\ .calendar-table td{background-color: #337AB7;}\
            .redTr{color:red;text-align:center; font-size: 18px;font-weight: bold;}\
            .blueTr{color:white;text-align:center;font-size: 14px;};");
        }
        //生成整个日历的div，包括年月份的div和日历表格都放在这个div里面
        c.buildPart({
            node : 'div',
            id : 'calendarDiv',
            className : 'calendar',
            appendToBox : calendarId//整个日历添加到提供进来的要放日历的id里面
        });
        //生成存储日历的年月份的div
        c.buildPart({
            node : 'div',
            id : 'headId',
            className : 'box',
            appendToBox : calendarDiv
        });
        //生成存储减少月份的span
        c.buildPart({
            node : 'span',
            id : 'dec',
            className : 'box-dec glyphicon glyphicon-chevron-left',
            appendToBox : headId
        });
        //生成存储年份的span
        c.buildPart({
            node : 'span',
            id : 'Year',
            innerHTML : thisyear + '年',
            appendToBox : headId
        });
        //生成存储月份的span
        c.buildPart({
            node : 'span',
            id : 'Month',
            innerHTML : thismonth < 10 ? '0'+ thismonth + '月' : thismonth,
            appendToBox : headId
        });
        //生成存储增加月份的div
        c.buildPart({
            node : 'span',
            id : 'add',
            className : 'box-add glyphicon glyphicon-chevron-right',
            appendToBox : headId
        });
        //生成存储日历的table
        c.buildPart({
            node : 'table',
            id : 'calendarTable',
            className : 'calendar-table table-bordered ',
            innerHTML : CalendarTr,
            appendToBox : calendarDiv
        });
    }
    //内部初始化日历生成
    c.init();
    //日历的事件部分
    (function(){
        var clickDate;//记录翻页后的日期
        var dec = document.getElementById('dec');
        var add = document.getElementById('add');
        var Month = document.getElementById('Month');
        var Year = document.getElementById('Year');
        var Tr = document.getElementById('calendarTable');
        month = parseInt(Month.innerHTML);
        year = parseInt(Year.innerHTML);
        //设置月份减少的点击事件
        dec.onclick = function(e){
            e = e || event;
            var target = e.target || e.srcElement;
            if(target.id == 'dec'){
                if(month>1 || month== 12){
                    Month.innerHTML = --month < 10 ? '0' + month+ '月': month+ '月';
                    CalendarTr = c.getcalendar(year,month);
                    Tr.innerHTML = '';
                    Tr.innerHTML = CalendarTr;
                }else{
                    Year.innerHTML  = --year <=1970 ? 1970 + '年' : year + '年';
                    Month.innerHTML = year < 1970 && month < 2 ? '01月': 12 + '月';
                    //小于1970的时间显示为1970的时间
                    if(year < 1970 && month < 2){
                        year = 1970,month=1;
                        CalendarTr = c.getcalendar(year,month);
                        Tr.innerHTML = '';
                        Tr.innerHTML = CalendarTr;
                    }else{
                        month = 12;
                        CalendarTr = c.getcalendar(year,month);
                        Tr.innerHTML = '';
                        Tr.innerHTML = CalendarTr;
                    }
                    
                }
            }
            clickDate = String(year) + "-" +String(month<10? '0'+month : month);
            //alert(clickDate);
        }
        //设置月份增加的点击事件
        add.onclick = function(e){
            e = e || event;
            var target = e.target || e.srcElement;
            if(target.id == 'add'){
                if(month>0 && month<12){
                    Month.innerHTML = ++month < 10 ? '0' + month+ '月': month+ '月';
                    CalendarTr = c.getcalendar(year,month);
                    Tr.innerHTML = '';
                    Tr.innerHTML = CalendarTr;
                }else{
                    Year.innerHTML  = ++year + '年';
                    Month.innerHTML = '0'+1 + '月';
                    month = 1;
                    CalendarTr = c.getcalendar(year,month);
                    Tr.innerHTML = '';
                    Tr.innerHTML = CalendarTr;
                }
            }
            clickDate = String(year)+ "-" +String(month<10? '0'+month : month);
            /*alert(clickDate);*/
        }
    })();
}
window.calendar = calendar;
})();