
function drawTemplate(options) {
	var $target = options.$target,//被填充的jQuery对象
		template = options.template,//html模板
		data = options.data;//viewModel类型的json数据
	var html = "";
	var pageIndex = data.pageIndex;
	var pageSize = data.pageSize;
	for (var i = 0; i < data.result.length; i++) {
		var propItemObj = data.result[i];
		var itemHtml = template;
		for (var key in propItemObj) {
			var reg = new RegExp("\{" + key + "\}", "g");// 实现全局替换
			itemHtml = itemHtml.replace(reg, propItemObj[key]);
		}
		itemHtml = itemHtml.replace("\{index\}",  (pageIndex * pageSize) + i + 1);
		html += itemHtml;
	}
	html = replaceToNull(html);//将模板中无法替换的占位符变为 ""
	$target.html(html);
}

var pageLength = 5;
var startPage = 1;// 定义开始页
var endPage = pageLength;
function drawPageComponent(options) {	
	var functionName = options.selectFunction ? options.selectFunction : "selectPage";
	var $target = options.$target;
	var pageControl = $("<ul class='pagination'></ul>");
	var data = options.data;
    pageControl.html("");
    var pageSize = data.pageSize;
    var total = data.total;
    var pageCount = Math.ceil(total / pageSize);// 向上取整页数
    var pageIndex = data.pageIndex + 1;
    if (pageIndex < pageLength) {
        startPage = 1;
        endPage = pageLength;
    } else {
        startPage = pageIndex - 2;
        endPage = Math.min(startPage + pageLength - 1, pageCount);
    }
    var li = document.createElement("li");
    var a = document.createElement("a");
    a.setAttribute("onclick", functionName + "(this, 1)");
    a.innerText = "首頁";
    li.appendChild(a);
    pageControl.append(li);
    li = document.createElement("li");
    a = document.createElement("a");
    if (pageIndex == 1) {
        li.className = "disabled";
        li.innerHTML = "<span aria-hidden='true'>上一頁</span>";
    } else {
        a = document.createElement("a");
        a.setAttribute("aria-label", "Previous");
        a.setAttribute("onclick", functionName + "(this," + (pageIndex - 1) +")");
        a.innerHTML = "<span aria-hidden='true'>上一頁</span>";
        li.appendChild(a);
    }
    pageControl.append(li);
    if (startPage > 1) {
        li = document.createElement("li");
        li.innerHTML = "<span>...</span>";
        pageControl.append(li);
    }
    var len = pageCount - startPage < pageLength ? pageCount - startPage + 1 : pageLength;
    if(len < 5 && endPage == pageCount && pageIndex >= 5){
    	startPage = pageCount - 4;
    	len = 5;
    }
    for (var index = startPage; index < len + startPage; index++) {
        li = document.createElement("li");
        a = document.createElement("a");
        if (pageIndex == index) {
            li.className = "active";
        }
        a.setAttribute("onclick", functionName + "(this," + index +")");
        a.innerText = index;
        li.appendChild(a);
        pageControl.append(li);
    }
    if (endPage < pageCount) {
        li = document.createElement("li");
        li.innerHTML = "<span>...</span>";
        pageControl.append(li);
    }
    if (pageIndex >= pageCount) {
        li = document.createElement("li");
        li.className = "disabled";
        li.innerHTML = "<span aria-hidden='true'>下一頁</span>";
        pageControl.append(li);
    } else {
        li = document.createElement("li");
        a = document.createElement("a");
        a.setAttribute("onclick", functionName + "(this," + (pageIndex + 1) +")");
        a.innerHTML = "<span aria-hidden='true'>下一頁</span>";
        li.appendChild(a);
        pageControl.append(li);
    }
    li = document.createElement("li");
    a = document.createElement("a");
    a.setAttribute("onclick", functionName + "(this," + pageCount +")");
    a.innerText = "尾頁"
    li.appendChild(a);
    pageControl.append(li);
	$target.html(pageControl);
}
/**
 * 将模板中无法替换的占位符变为 ""
 * @param template 模板
 * @returns
 */
function replaceToNull(template) {
	var reg2 = new RegExp("\{[a-zA-Z0-9_]*\}", "g");
	return template.replace(reg2,"");
}