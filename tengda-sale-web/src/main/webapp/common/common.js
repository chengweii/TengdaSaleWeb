var loadingModal = {
    show: function (loadText) {
        if ($('#loadingModal').attr("show") == true) {
            return;
        }
        $('#loadingModal').modal({backdrop: 'static', keyboard: false});
        $('#loadingModal').attr("show", true);
    }, hide: function () {
        if ($('#loadingModal').attr("show") == false) {
            return;
        }
        $('#loadingModal').modal('hide');
        $('#loadingModal').attr("show", false);
    }
};

function renderList(listConfig) {
    loadingModal.show();
    $.ajax({
        type: "post",
        url: listConfig.url,
        contentType: "application/json;charset=utf-8",
        data: JSON.stringify(listConfig.params),
        dataType: 'json',
        cache: false,
        success: function (result) {
            if (result && result.code == 200) {
                $(".container").html("");

                var html = "";
                html += "<h3><a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-primary btn-sm btn-add\">添加</a></h3>";

                if (!result || !result.result) {
                    html += "<div class=\"alert alert-warning\" role=\"alert\"><span class=\"glyphicon glyphicon-info-sign\" aria-hidden=\"true\"></span> 没有查询到数据，请添加。</div>";
                    $(".container").html(html);
                    loadingModal.hide();
                    return;
                }

                var data = result.result.content;

                var table = $("<table class=\"table table-bordered table-striped\"></table>");
                var th = $("<tr></tr>");
                for (var index in listConfig.headers) {
                    th.append("<th>" + listConfig.headers[index] + "</th>");
                }
                th.append("<th>操作</th>");
                table.append(th);

                for (var index in data) {
                    var tr = $("<tr></tr>");
                    for (var indexC in listConfig.attrNames) {
                        tr.append("<td>" + data[index][listConfig.attrNames[indexC]] + "</td>");
                    }
                    tr.append(" <a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-sm btn-danger btn-remove\" style=\"margin:3px;\" item-id=\"" + data[index]["id"] + "\"> 删除</a>");
                    table.append(tr);
                }

                $(".container").append(html);
                $(".container").append(table);

                $(".container").append("<div class=\"paginator\" style=\"text-align: center\"></div>");
                $(".paginator").pagination({
                    currentPage: result.result.number + 1 || 1,
                    displayedPages: 5,
                    pages: result.result.totalPages,
                    itemsOnPage: result.result.numberOfElements,
                    items: result.result.totalElements,
                    cssStyle: 'light-theme',
                    onPageClick: function (pageNumber, event) {
                        listConfig.params.pageNo = pageNumber;
                        renderList(listConfig);
                    }
                });
                $(".paginator .prev").html("上一页");
                $(".paginator .next").html("下一页");

                if (listConfig.callback) {
                    listConfig.callback(result);
                }

                loadingModal.hide();
                console.log("renderList finished.");
            } else {
                console.log("renderList failed.");
            }
        }
    });
}

function renderAddForm(data) {
    console.log("renderAddForm...");
}

function renderRemove(removeConfig, listConfig) {
    $(".btn-remove").click(function () {
        var item = $(this);
        loadingModal.show();
        $.ajax({
            type: "post",
            url: removeConfig.url + "/" + item.attr("item-id"),
            data: removeConfig.params,
            dataType: 'json',
            cache: false,
            success: function (result) {
                if (!result || result.code != 200) {
                    toastr.error('删除失败');
                } else {
                    toastr.success('删除成功');
                }
                renderList(listConfig);
                loadingModal.hide();
            }
        });
    });
    console.log("renderRemove finished.");
}

function renderModule(listConfig, removeConfig, modifyConfig) {
    listConfig.callback = function () {
        renderRemove(removeConfig, listConfig);
    };
    renderList(listConfig);
}