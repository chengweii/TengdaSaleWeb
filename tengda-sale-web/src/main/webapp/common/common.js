var loadingModal = {
    show: function (loadText) {
        $('#loadingModal').modal({backdrop: 'static', keyboard: false});
    }, hide: function () {
        $('#loadingModal').modal('hide');
    }
};

function renderList(listConfig) {
    loadingModal.show();
    $.ajax({
        type: "post",
        url: listConfig.url,
        data: listConfig.params,
        dataType: 'json',
        cache: false,
        success: function (result) {
            if (result && result.code == 200) {
                $(".container").html("");

                var data = result.result;
                var html = "<h1>" + listConfig.title + "</h1><hr/>";
                html += "<h3>数据列表 <a href=\"javaScript:void(0);\" type=\"button\" class=\"btn btn-primary btn-sm btn-add\">添加</a></h3>";

                if (!data || data.length <= 0) {
                    html += "<div class=\"alert alert-warning\" role=\"alert\"><span class=\"glyphicon glyphicon-info-sign\" aria-hidden=\"true\"></span> 没有查询到数据，请添加。</div>";
                    $(".container").html(html);
                    loadingModal.hide();
                    return;
                }

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
                loadingModal.hide();
                item.parent().remove();
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