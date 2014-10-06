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
 * This file is part of the x3mlEditor webapp of Mapping Memory Manager project.
 */
//var path;
var load_url = "Mapping";
var lastValue;
//var uploadMessage = "Upload file";
var id, comboAPI;
var goAhead = true;
$(document).ready(function() {

    //help();
    editable();
   
});
function  confirmDialog() {
    confirmDialog("");
}



function  confirmDialog(type) {
    if (type === "Mapping") {
        goAhead = confirm("This action will delete all mapping's contents! Are you sure?");
    } else {
        goAhead = confirm("Are you sure?");
    }
}



function action(url, generator) {
    url = url+"&generator="+$("#generator").val();
    
    if (goAhead) {

        $.post(url)
                .done(function(data) {
//                    location.reload();
                    $("body").html(data);
                    //   help();
                    editable();
                  
                });
    } else {
        goAhead = true;
    }

}

function editable() {
    $.editable.addInputType('optgroup', {
        element: function(settings, original) {
            var select = $('<select />');
            $(this).append(select);
            return (select);
        },
        content: function(data, settings, original) {
            if (String == data.constructor) {
                eval('var json = ' + data);
            } else {
                var json = data;
            }

            var addto = $('select', this);
            $.each(json, function(i, optgroups) {
                $.each(optgroups, function(groupName, options) {
                    var $optgroup = $("<optgroup>", {
                        label: groupName
                    });

                    $optgroup.appendTo(addto);

                    $.each(options, function(j, option) {
                        var $option = $("<option>", {
                            text: option.name,
                            value: option.id
                        });

                        if (option.selected !== null && option.selected !== undefined && option.selected) {
                            $option.attr('selected', 'selected');
                        }
                        $option.appendTo($optgroup);
                    });
                    // alert($optgroup.html())
                });
            });
        }


    })

//  path = $(location).attr('pathname');


    $('*[data-generator_editable]').each(function() {

        var $this = $(this);
        $this.addClass("editable");
        var xpath = $this.attr("data-path");
        var type = $this.attr("data-generator_editable");
        var placeholderColor = "";
        if (xpath.indexOf("/domain/") !== -1 && xpath.indexOf("/additional") === -1) {
//            placeholderColor = "black";
        }

        var lastPart = /[^/]*$/.exec(xpath)[0];

        if (type === "select") {
            $this.editable("Mapping"
                    ,
                    {
                        submitdata: {
                            xpath: xpath,
                            id: id
                        },
//                        callback: function(value) {
//                            lastValue = value;
//                            alert(lastValue)
//                        },
                        indicator: 'Saving...',
                        placeholder: '<span class="placeholder ' + placeholderColor + '">(fill in ' + lastPart + ')</span>',
                        loadurl: 'Mapping?xpath=' + xpath + "&id=" + id + "&mode=" + comboAPI,
//                        loadurl: loadurl(xpath, id),
                        type: "optgroup",
                        tooltip: 'Click to edit ' + xpath,
                        style: "inherit",
                        onblur: "submit"



//          
                    });
        } else if (type === "textarea") {
            $this.editable("Mapping"
                    ,
                    {
                        submitdata: {
                            xpath: xpath,
                            id: id
                        },
                        indicator: 'Saving...',
                        placeholder: '<span class="placeholder ' + placeholderColor + '">(fill in ' + lastPart + ')</span>',
                        tooltip: 'Click to edit ' + xpath,
                        type: "textarea",
                        rows: 3,
                        style: "inherit",
//                        cols: 80,
                        onblur: 'submit'
                    });
        } else if (type === "operators") {
            $this.editable("Mapping"
                    ,
                    {
                        submitdata: {
                            xpath: xpath,
                            id: id
                        },
                        callback: function(value) {
                            var $this = $(this);
                            if (value.trim() === "noValue") {
                                $this.next().hide();
                            } else {
                                $this.next().show();
                            }

                        },
                        indicator: 'Saving...',
                        placeholder: '<span class="placeholder ' + placeholderColor + '">(fill in ' + lastPart + ')</span>',
                        data: " {'=':'=','noValue':'noValue','nt':'nt'}",
                        type: "select",
                        tooltip: 'Click to edit ' + xpath,
                        style: "inherit",
                        onblur: 'submit'
//          
                    });
        } else {

            $this.editable("Mapping"
                    ,
                    {
                        submitdata: {
                            xpath: xpath,
                            id: id
                        },
                        indicator: 'Saving...',
                        placeholder: '<span class="placeholder ' + placeholderColor + '">(fill in ' + lastPart + ')</span>',
                        tooltip: 'Click to edit ' + xpath,
                        onblur: 'submit',
                        style: "inherit",
                        height: "100%" //to fix Chrome-Safari issue
//                        width:($this.width() + 100) + "px", 
                    });
        }


    });
}



//$(document).ajaxSend(function(r, s, t) {
//    alert("'" + t.url + "'");
//    if ($.trim(t.url) === load_url + "?id=editable")
//       $('#loading').show();
//});
function closeAndUnlock(id) {
//Decided tha popup is no longer necessary...
    jQuery.ajax({
        url: 'Mapping?action=close&id=' + id,
        async: false
    });
}

function get_XPath(elt) {
    var path = '';

    for (; elt && elt.nodeType == 1; elt = elt.parentNode) {
        var idx = $(elt.parentNode).children(elt.tagName).index(elt) + 1;
        idx > 1 ? (idx = '[' + idx + ']') : (idx = '');
        //idx='['+idx+']';
        path = '/' + elt.tagName + idx + path;
    }
    return path.substr(1);
}

function help() {
//    var abc = $("*[data-help]");
//    var out="";
//    for (var i=0;i<abc.length;i++) {
//        
//        out = out+"\n"+$(abc[i]).attr("data-help");
//    }
//    alert(out)
    $("*[data-help]").each(function() {
        var $this = $(this);
        var field = $this.attr("data-help");
        field = "\"" + field + "\"" + ");return false;";

        var html = "<a class='inline' title='Help' href='#' onclick='getHelpFor(" + field + "'>" +
//                '");return false;'>' +
                "<img  src='formating/images/help16.png'/></a>";
        $this.append($(html));
    });
}

function getHelpFor(field) {

    $.post("Mapping?action=help&field=" + field)
            .done(function(data) {
//                alert(data);
            });
}





