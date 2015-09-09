/*
 * Copyright 2014 Institute of Computer Science,
 * Foundation for Research and Technology - Hellas
 *
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved
 * by the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence.
 * You may obtain a copy of the Licence at:
 *
 * http://ec.europa.eu/idabc/eupl
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the Licence is distributed on an "AS IS" basis,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the Licence for the specific language governing permissions and limitations
 * under the Licence.
 *
 * Contact:  POBox 1385, Heraklio Crete, GR-700 13 GREECE
 * Tel:+30-2810-391632
 * Fax: +30-2810-391638
 * E-mail: isl@ics.forth.gr
 * http://www.ics.forth.gr/isl
 *
 * Authors : Georgios Samaritakis, Konstantina Konsolaki.
 *
 * This file is part of the 3MEditor webapp of Mapping Memory Manager project.
 */

var allowedExtensions = ['xml', 'rdf', 'rdfs', 'html'];
var comboAPI, goAhead;
var clipboard = {
    "mapping": "",
    "link": ""
};


$(document).ready(function() {


    $('.description').popover({
        trigger: "hover",
        placement: "right"
    });

    $("#matching_table").on("click", ".collapseExpand", function() {
        $(this).parentsUntil("thead").parent().next("tbody").children("tr.path, tr.range").toggle();
    });

    if (mode === 0) {
        comboAPI = $('#targetAnalyzer input:radio:checked').val()


        $('#table_view-btn').click(function() {

            $("body").css("opacity", "0.4");

            var url = "GetPart?id=" + id + "&part=mappings&mode=view";
            var $btn = $(this);
            $btn.button('loading');
            $.post(url).done(function(data) {
                $("#matching_table>div.mappings").html(data);
                $btn.button('reset');
                $('.description').popover({
                    trigger: "hover",
                    placement: "right"
                });

                $('.collapseExpand').click(function() {
                    $(this).parentsUntil(".empty").parent().prevAll("tr.path, tr.range").toggle();
                });
                $(".empty").find("div.row").css("display", "block");
                $("body").css("opacity", "1");


            });

        });


        $('#collapseExpandAll-btn').click(function() {
            $("tr.path, tr.range").toggle();
        });


        $('#scrollTop-btn').click(function() {
            $("html, body").animate({scrollTop: 0}, "slow");
        });
        $('#scrollBottom-btn').click(function() {
            $("html, body").animate({scrollTop: $(document).height()}, "slow");
        });

        $('#info_rawXML-btn, #rawXML-btn').click(function() {
            $("#myModal").find("textarea").val("");
            var xpath = "";

            if ($(".active").children("a").html() === "Info") {
                xpath = "//x3ml/*[name()='info' or name()='namespaces']";

            } else {
                if ($(".edit").length === 0) { //All
                    xpath = "//x3ml/mappings";
                } else if ($(".edit").length === 1) { //Domain
                    xpath = $(".edit").attr("data-xpath");
                } else if ($(".edit").length === 2) {//Link
                    xpath = $(".edit").attr("data-xpath");
                    xpath = xpath + "/.."
                } else if ($(".edit").length === 3) {//New map
                    xpath = $(".edit").attr("data-xpath");
                    xpath = xpath + "/.."
                }
            }
            $("#myModal").find(".xpath").html(xpath);

            var url = "Action?id=" + id + "&xpath=" + xpath + "&action=raw";
            $.post(url).done(function(data) {
                $("#myModal").find("textarea").val(data);
            });


            $("#myModal").modal('show');

        });



        var $scrollingDiv = $('#table_view-btn');

        $(window).scroll(function() {
            $scrollingDiv
                    .stop()
                    .animate({"marginTop": ($(window).scrollTop())}, "fast");
        });
    }



});

$('.saveXML-btn').click(function() {
    if (confirm("This is an action that may cause data loss. Are you sure you want to proceed?") === true) {

        var value = $(".modal-body").children("textarea").val();
        var xpath = $("#myModal").find(".xpath").html();


        var url = "Update?id=" + id + "&xpath=" + xpath + "&action=raw";
        $.post(url, {value: value}).done(function(data) {


            if (data.indexOf("Update complete!)" !== -1)) {
                if (xpath.indexOf("'namespaces'") !== -1) {
                    var infoVisible = $("#info_edit-btn").is(":visible");
                    var mode = "edit";
                    if (infoVisible === true) {
                        mode = "view";
                    }
                    var url = "GetPart?id=" + id + "&part=info&mode=" + mode;
                    $.post(url).done(function(data) {
                        $("#info>div").html(data);
                    });

                } else if (xpath.endsWith("/domain/..") || xpath.endsWith("/mappings")) {

                    if (xpath.endsWith("/mappings")) {
                        var url = "GetPart?id=" + id + "&xpath=" + xpath + "&mode=view&targetAnalyzer=" + comboAPI + "&sourceAnalyzer=" + sourceAnalyzer;
                        $.post(url).done(function(data) {
                            $(".mappings").replaceWith(data);
                        });
                    } else {
                        viewOnly(); //Would be tough otherwise
                    }
                } else {

                    var editPath;
                    if (xpath.endsWith("/domain")) {
                        editPath = xpath;
                    } else if (xpath.endsWith("/path/..")) {
                        editPath = xpath.replaceAll("/path/..", "");
                    }

                    //First make clicked part editable
                    var url = "GetPart?id=" + id + "&xpath=" + editPath + "&mode=edit&targetAnalyzer=" + comboAPI + "&sourceAnalyzer=" + sourceAnalyzer;
                    $.post(url).done(function(data) {

                        if (xpath.endsWith("/domain")) {
                            $(".edit").replaceWith(data);
                        } else if (xpath.endsWith("/path/..")) {
                            $(".edit").remove();
                            $(".dummyDomain").after(data);
                        }
                        fillCombos();
                        $(".types").each(function() {
                            var $this = $(this);
                            if ($this.children(".type").length === 1) {
                                $this.children(".type").find(".input-group-btn").css("visibility", "hidden");
                            }
                        });

                    });

                }



            }
            alert(data); //DO NOT DELETE! Useful alert!
            $('#myModal').modal('hide');
        });
    }
});


$('.nav a').click(function(e) {
    e.preventDefault()
    if ($(this).html() === "About") {
        $("#about").load("readme.html");
    } else if ($(this).html() === "Graph") {
        $("#graph").load("graph.html");
    } else if ($(this).html() === "Transformation") {
        $("#x3mlEngine").load(("x3mlEngine.html"), function() {
            var sourceFilename = $("a:contains('view xml')").attr("title");

            var url = "FetchBinFile?file=" + sourceFilename;
            $.post(url, "xml").done(function(xml) {
                var xmlString = (new XMLSerializer()).serializeToString(xml);

                $("#sourceFile").val(xmlString);
            });
        });

    }
})

$("body").on("click", "#runEngine", function() {

    $("#engineResult").html("");

    var output = $(".outputFormat  label.active input").val();
    $(".loader").show();

    var source = $("#sourceFile").val();
    var url = "/Mapper/Index?id=" + id + "&generator=" + $("#generator").val() + "&uuidSize=" + $("#uuidSize").val() + "&output=" + output;

    $.post(url, {sourceFile: source}, "html").done(function(data) {


        data = String(data).replace(/&/g, '&amp;').replace(/</g, '&lt;').replace(/>/g, '&gt;').replace(/"/g, '&quot;');
        $(".loader").hide();
        $("#engineResult").html(data);
    });
});
function  confirmDialog() {
    confirmDialog("");
}

function viewGraph() {
    window.open('/MappingAnalyzerServer/singlex3ml.html?id=' + id, '_blank');
    return false;
}


function  confirmDialog(type) {
    if (type === "Mapping") {
        goAhead = confirm("This action will delete all mapping's contents! Are you sure?");
    } else {
        goAhead = confirm("Are you sure?");
    }
}


$("#matching_table").on("click", ".clickable", function() {
    if (mode === 0) {
        $("body").css("opacity", "0.4");
        var $path = $(this);
        if ($path.hasClass("empty")) {

            $("body").css("opacity", "1");
        } else {


//First make clicked part editable
            var url = "GetPart?id=" + id + "&xpath=" + $path.attr("data-xpath") + "&mode=edit&targetAnalyzer=" + comboAPI + "&sourceAnalyzer=" + sourceAnalyzer;
            $.post(url).done(function(data) {

//Adding header and domain to help editing
                var $data = $(data);
                //Creating buttonGroup from hidden actions cell
                var $buttonGroup = $data.find(".actions");
                $buttonGroup.children("div").addClass("btn-group-xs").removeClass("btn-group-vertical");
                $buttonGroup.find("button").addClass("btn-default").removeClass("close").removeClass("closeLike").addClass("btn-xs").removeClass("btn-sm");
                $buttonGroup.find("span").removeClass("smallerIcon");
                $buttonGroup.find("button").last().addClass("closeOnHeader");
                var buttonGroupHtml = $buttonGroup.html();
                buttonGroupHtml = buttonGroupHtml.replaceAll("<br>", "");
                //Creating dummy header row and adding buttonGroup
                var $head = $("thead").clone();
                $head.find("button").remove(); //Removing umwanted collapse/expand button
                var $newHead = $head.find("th").slice(0, 5);
                var $newHeadCells = $("<tr/>").append($newHead);
                $newHeadCells.find("th").last().append($(buttonGroupHtml));
                var theadRow = "<tr class='dummyHeader'>" + $newHeadCells.html() + "</tr>";
                var $theadRow = $(theadRow);
                var domainRow = "<tr class='dummyDomain'/>";
                $path.hide();
                if ($path.hasClass("domain")) {

//Show/hide paste accordingly
                    if (clipboard["mapping"] === "") {
                        $theadRow.find(".paste").hide();
                    }
                } else {
                    if ($path.hasClass("path")) {
                        $path.next().remove();
                    } else {
                        $path.prev().remove();
                    }
//Also add domainrow
                    domainRow = "<tr class='dummyDomain'>" + $path.siblings(".domain").html() + "</tr>";
                    //Show/hide paste accordingly
                    if (clipboard["link"] === "") {
                        $theadRow.find(".paste").hide();
                    }
                }
                theadRow = "<tr class='dummyHeader'>" + $theadRow.html() + "</tr>";
                var helpRows = theadRow + domainRow;
                $path.replaceWith(helpRows + data);
                $path.fadeIn(500);
                fillCombos();
                $(".types").each(function() {
                    var $this = $(this);
                    if ($this.children(".type").length === 1) {
                        $this.children(".type").find(".input-group-btn").css("visibility", "hidden");
                    }
                });
                $("body").css("opacity", "1");
                $(document.body).animate({//Scroll new editable block to top of page - 100px
                    'scrollTop': $('.dummyHeader').offset().top - 100

                }, 2000);
            });
            viewOnly();
        }
    }
});
jQuery.fn.swapWith = function(to) {
    return this.each(function() {
        var copy_to = $(to).clone(true);
        var copy_from = $(this).clone(true);
        $(to).replaceWith(copy_from);
        $(this).replaceWith(copy_to);
    });
};
function viewOnlySpecificPath(xpath) {

    $("#matching_table").find("tr[data-xpath='" + xpath + "']").each(function(index) {
        var $row = $(this);
        var url = "GetPart?id=" + id + "&xpath=" + $(this).attr("data-xpath") + "&mode=view";
        $.post(url).done(function(data) {

            $row.hide();
            $row.replaceWith(data);
            $row.fadeIn(500);
        });
    });
}


function viewOnly() {
//Then make previously edited part viewable...
    $("#matching_table").find(".edit").each(function(index) {
        var $row = $(this);
        if (typeof $(this).attr("data-xpath") !== 'undefined') { //if path===undefined abort useless request


            var url = "GetPart?id=" + id + "&xpath=" + $(this).attr("data-xpath") + "&mode=view";
            $.post(url).done(function(data) {

                $row.hide();
                $row.replaceWith(data);
                $row.fadeIn(500);
            });
        }
    });
    //Also hide help rows
    $(".dummyHeader").remove();
    $(".dummyDomain").remove();
}
function refreshCombos(updatedXpath, self) {

    var pathFound = false;
    $('.edit').find('input.select2').each(function(index) { //Needs refining!
        var xpath = $(this).attr('data-xpath');
        if (updatedXpath === xpath) {
            pathFound = true;
            if (self) {
                fillCombo($(this), true);
            }
        } else {
            if (pathFound === true) {
                fillCombo($(this), false);
            }
        }
        console.log(updatedXpath + " vs " + xpath + " PATH FOUND:" + pathFound);
    });
}



function fillCombo($this, setValue) {

    var xpath = $this.attr('data-xpath');
    if (typeof $this.attr("data-xpath") !== 'undefined') {

        var url;
        if (xpath.indexOf("/source_relation") !== -1 || xpath.indexOf("/source_node") !== -1) {
            fillSourceCombo($this);
        } else {
            url = 'GetListValues?id=' + id + '&xpath=' + xpath + '&targetAnalyzer=' + comboAPI;
            $.ajax({
                url: url,
                dataType: 'json'
            }).success(function(data) {
                json = data;
                if (setValue) {
                    var oldValue = $this.val().trim();
                    var wrongValue = false;
                    if (JSON.stringify(json).indexOf('"' + oldValue + '"') === -1) {
                        wrongValue = true;
                    }

                    $this.select2({
                        allowClear: true,
                        placeholder: "Select a value",
                        data: json,
                        initSelection: function(element, callback) {
                            var data = {id: $this.attr("data-id"), text: $this.val()};
                            callback(data);
                        }
                    })

                    if (wrongValue) {
                        $this.parent().find(".select2-chosen").html("<span style='color:red;'>Value " + oldValue + " is no longer valid!</span>");
                    } else {
                        $this.select2('val', $this.val());
                    }

                } else {

                    var oldValue = $this.select2('val');
                    if (typeof oldValue === 'object') {
                        $this.select2({
                            allowClear: true,
                            placeholder: "Select a value",
                            data: json
                        })
                        oldValue = $this.parent().find(".select2-chosen").html();
                        if (JSON.stringify(json).indexOf('"' + oldValue + '"') === -1) {
                            if (oldValue.indexOf(":") !== -1) {
                                oldValue = oldValue.substring(oldValue.indexOf(':') + 1)
                            }
                            $this.parent().find(".select2-chosen").html("<span style='color:red;'>Value " + oldValue + " is no longer valid!</span>");
                        }
                        $("div[title='" + xpath + "']").first().remove();
                    } else {
                        $this.select2({
                            allowClear: true,
                            placeholder: "Select a value",
                            data: json,
                        })

                        if ($this.parent().find(".select2-chosen").html() === "&nbsp;") {
                            $this.parent().find(".select2-chosen").html("<span style='color:red;'>Value " + oldValue.substring(oldValue.indexOf(':') + 1) + " is no longer valid!</span>");
                        }
                    }
                }
                $(".loader").hide();
            });
        }

    }

}
function displayCurrentValue(selectedObject, currentSearchTerm) {
    return currentSearchTerm;
}


function fillSourceCombo($this) {


    if (sourceAnalyzerPaths.length === 0) {

        var url = '/SourceAnalyzer/filePathService';
        var sourceAnalyzerFile = "";
        if (sourceAnalyzerFiles.indexOf("***") !== -1) {
            var files = sourceAnalyzerFiles.split("***");
            var schemaFile = files[0];
            var instanceFile = files[1];
            if (schemaFile.length > 0) {
                sourceAnalyzerFile = schemaFile;
            } else {
                if (instanceFile.length > 0) {
                    sourceAnalyzerFile = instanceFile;
                }
            }



        }
        if (sourceAnalyzer === "on") {

            if (sourceAnalyzerFile.endsWith(".xsd")) {
                sourceAnalyzerFile = "../xml_schema/" + sourceAnalyzerFile;
            }

            $.post(url, {fileName: sourceAnalyzerFile}, function(data) {
                sourceAnalyzerPaths = data.results;
                var xpath = $this.attr("data-xpath");
                var filteredPaths = sourceAnalyzerPaths;
                if (!xpath.endsWith("domain/source_node")) {
                    filteredPaths = filterSourceValues(xpath);
                }

                $this.select2({
                    allowClear: true,
                    nextSearchTerm: displayCurrentValue,
                    placeholder: "Select a value",

                    createSearchChoice: function(term, data) {
                        if ($(data).filter(function() {
                            return this.text.localeCompare(term) === 0;
                        }).length === 0) {
                            return {
                                id: term,
                                text: term
                            };
                        }
                    },
                    data: filteredPaths,
                    initSelection: function(element, callback) {
                        var data = {id: element.attr("data-id"), text: element.val()};
                        callback(data);
                    }
                });
            },
                    "json").error(function() {
                alert("Error reading source schema or source xml. Please disable Source Analyzer from Configuration tab to fill in source values.");
            });
        }
    } else {
        var xpath = $this.attr("data-xpath");
        var filteredPaths = sourceAnalyzerPaths;
        if (!xpath.endsWith("domain/source_node")) {
            filteredPaths = filterSourceValues(xpath);
        }
        $this.select2({
            allowClear: true,
            nextSearchTerm: displayCurrentValue,
            placeholder: "Select a value",
            createSearchChoice: function(term, data) {
                if ($(data).filter(function() {
                    return this.text.localeCompare(term) === 0;
                }).length === 0) {
                    return {
                        id: term,
                        text: term
                    };
                }
            },
            data: filteredPaths,
            initSelection: function(element, callback) {
                var data = {id: element.attr("data-id"), text: element.val()};
                callback(data);
            }
        });
    }
    $(".loader").hide();
}

function filterSourceValues(xpath) {

    var domainPath = xpath.replace(/\/link[\s\S]*/g, "/domain");
    var $domain = $("tr[data-xpath='" + domainPath + "']");

    var $domainDiv = $domain.find(".nextToIcon");
    
    var domainValue = $domainDiv.html();
    if ($domainDiv.children("span").length>0) {
       domainValue = $domainDiv.children("span").attr("title");
    }

    var filteredPaths = $.grep(sourceAnalyzerPaths, function(item) {
        if (item.id.startsWith(domainValue + "/")) {
            return item;
        }
    });


    return filteredPaths;

}

function fillCombos() {
    $(".edit").find('.select2').each(function(index) {
        fillCombo($(this), true);
    });
}

$("#targetAnalyzer input:radio").change(function() { //On change set variable
    comboAPI = $(this).val();
    viewOnly();
});
$("#sourceAnalyzer input:radio").change(function() { //On change set variable
    sourceAnalyzer = $(this).val();
    viewOnly();
});



$('#info_edit-btn').click(function() {
    $("body").css("opacity", "0.4");
    var url = "GetPart?id=" + id + "&part=info&mode=edit";
    var $btn = $(this);
    $btn.button('loading');
    $.post(url).done(function(data) {
        $("#info>div").html(data);
        $btn.toggle();
        $btn.button('reset');
        $('#info_view-btn').toggle();



        $('.fileUpload').each(function() {
            var $this = $(this);
            upload($this);
        });
        if ($(".target_info").length === 1) { //Hide delete target if there only one!
            $(".targetInfoDeleteButton").hide();
        }

        $("body").css("opacity", "1");
    });
});


$('#info_view-btn').click(function() {
    $("body").css("opacity", "0.4");
    var url = "GetPart?id=" + id + "&part=info&mode=view";
    var $btn = $(this)
    $btn.button('loading');
    $.post(url).done(function(data) {
        $("#info>div").html(data);
        $btn.toggle();
        $btn.button('reset');
        $('#info_edit-btn').toggle();
        $("body").css("opacity", "1");
    });
});
function upload($this) {

    var xpath = $this.attr("data-xpath");
    var uploadMessage = "Upload File";
    if (xpath.endsWith("schema_file")) {
        uploadMessage = "Upload File";
    } else if (xpath.endsWith("xml_link")) {
        uploadMessage = "Upload xml";
    } else if (xpath.endsWith("html_link")) {
        uploadMessage = "Upload html";
    } else if (xpath.endsWith("rdf_link")) {
        uploadMessage = "Upload rdf";
    }
    var mappingId = id;
    $this.fineUploader({
        request: {
            endpoint: 'UploadReceiver?id=' + id + '&path=' + xpath + '&mode=' + comboAPI

        },
        multiple: false,
        validation: {
            allowedExtensions: allowedExtensions

        },
        text: {
            uploadButton: '<span class="glyphicon glyphicon-upload"></span> ' + uploadMessage
        },
        failedUploadTextDisplay: {
            mode: 'custom',
            maxChars: 40,
            responseProperty: 'error',
            enableTooltip: true
        },
        debug: false
    }).on('complete', function(event, id, fileName, responseJSON) {

        if (responseJSON.success) {
            filename = responseJSON.filename;


            var linkText = "view";
            if (uploadMessage === "Upload rdf") {
                linkText = "view rdf"
            } else if (uploadMessage === "Upload xml") {

                sourceAnalyzer = "on";
                sourceAnalyzerFiles = sourceAnalyzerFiles.split("***")[0] + "***" + filename;
                sourceAnalyzerPaths = "";
                $("#sourceAnalyzer").children("label").removeClass("active");
                $("#sourceAnalyzer").find("input").removeAttr("checked");
                $("#sourceAnalyzer>#label5").addClass("active").removeClass("disabled");
                $("#sourceAnalyzer>#label5>input").attr("checked", "checked");
                viewOnly();

                $("a:contains('Transformation')").attr("href", "#x3mlEngine").parent().removeClass("disabled").removeAttr("title");
                linkText = "view xml"
            } else if (uploadMessage === "Upload html") {
                linkText = "view html"
            } else { //Not sure if I want a default analyzer
                if (xpath.endsWith("source_schema/@schema_file")) {
                    sourceAnalyzer = "on";
                    sourceAnalyzerFiles = filename + "***" + sourceAnalyzerFiles.split("***")[1];
                    sourceAnalyzerPaths = "";
                    $("#sourceAnalyzer").children("label").removeClass("active");
                    $("#sourceAnalyzer").find("input").removeAttr("checked");
                    $("#sourceAnalyzer>#label5").addClass("active").removeClass("disabled");
                    $("#sourceAnalyzer>#label5>input").attr("checked", "checked");
                    viewOnly();
                } else {

                    if (comboAPI === "0") {
                        comboAPI = 2;
                        $("#targetAnalyzer").children("label").removeClass("active");
                        $("#targetAnalyzer").find("input").removeAttr("checked");
                        $("#targetAnalyzer>#label2").addClass("active");
                        $("#targetAnalyzer>#label2>input").attr("checked", "checked");
                    }
                }
            }

            var linkHtml = "<a title='" + encodeURIComponent(filename) + "' style='position:relative;top:1px;' target='_blank' href='FetchBinFile?id=" + mappingId + "&amp;file=" + encodeURIComponent(filename) + "'>" + linkText + " </a>";
            var deleteHtml = " <button class='btn btn-default btn-link btn-sm deleteFile' type='button' title='Delete " + encodeURIComponent(filename) + "' id='delete***" + xpath + "'>" +
                    "<span class='glyphicon glyphicon-remove'></span>" +
                    "</button> ";
            var $html = $("<div>" + linkHtml + deleteHtml + "</div>");
            $this.next().css('display', 'inline');
            $this.before($html);
            $this.toggle("slow");
        }
    });
}
