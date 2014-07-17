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
 * This file is part of the 3M webapp of Mapping Memory Manager project.
 */

function getObj(objId) {
    return document.getElementById(objId)
}

function trim(str) {
    return str.replace(/^\s*|\s*$/g, "");
}

function goToPage(selectedInput, pages) {
    var selection = document.getElementById(selectedInput).value;

    if (!isNaN(selection) && selection != '') {

        var number = selection * 1; ////gia na ginei to string -> Integer to pollaplasiazoume epi 1 (*1)
        var pageNumbers = pages * 1;////gia na ginei to string -> Integer to pollaplasiazoume epi 1 (*1)

        if (number > pageNumbers) {
            alert(EgirosAri8mosSelidas);
            return;
        }



        else if (number < 1) {
            alert(EgirosAri8mosSelidas);
            return;
        }


        else {
            var frm = document.forms["searchPagingForm"];
            frm.newP.value = selection;
            frm.submit();
        }
    }
    else
        alert(EgirosAri8mosSelidas);
}

function submitSearchPagingForm(param, valueof) {

    var frm = document.forms["searchPagingForm"];
    if (param == 'move') {
        frm.move.value = valueof;
    }
    else {
        frm.newP.value = valueof;
    }
    frm.submit();
}

function goToSearchPage(selection, pages) {
    if ((event.which && event.which == 13) ||
            (event.keyCode && event.keyCode == 13)) {
        if (!isNaN(selection) && selection != '') {

            var number = selection * 1; ////gia na ginei to string -> Integer to pollaplasiazoume epi 1 (*1)
            var pageNumbers = pages * 1;////gia na ginei to string -> Integer to pollaplasiazoume epi 1 (*1)

            if (number > pageNumbers) {
                alert(EgirosAri8mosSelidas);
                return;
            }

            else if (number < 1) {
                alert(EgirosAri8mosSelidas);
                return;
            }


            else {
                var frm = document.forms["searchPagingForm"];
                frm.newP.value = selection;
                frm.submit();
            }
        }
        else
            alert(EgirosAri8mosSelidas);
    }

}

function addNewVocTerm(term, url, noTermMsg, termExistsMsg) {
    term = trim(term);
    if (term == '') {
        alert(noTermMsg);
        return false;
    }

    terms = document.getElementsByName('term');

    for (i = 0; i < terms.length; i++) {
        if (terms[i].value == term) {
            alert(termExistsMsg + '\'' + term + '\'');
            return false;
        }
    }
    submitFormTo('newTermFrm', url);

    return true;
}

function addCriterion(tableBodyId, rowId) {
    var tbody = getObj(tableBodyId);
    var row = getObj(rowId);
    var newRow = row.cloneNode(true);

    tbody.appendChild(newRow);
    newRow = tbody.rows[tbody.rows.length - 1];

    lastRow = tbody.rows[tbody.rows.length - 2];
    var newId = lastRow.cells[0].childNodes[0].value * 1 + 1;

    //first cell: parameter. Two childrens: checkbox and hidden
    newRow.cells[0].childNodes[0].checked = false;
    newRow.cells[0].childNodes[0].value = newId;
    newRow.cells[0].childNodes[1].value = newId;
    //second cell: input field select. Two childrens: select and select (hidden)

    //third cell: input field text value. Two childrens: text and button
    newRow.cells[2].childNodes[0].value = '';
}

function addOutput(tableBodyId, rowId) {
    var tbody = getObj(tableBodyId);
    var row = getObj(rowId);
    var newRow = row.cloneNode(true);
    tbody.appendChild(newRow);
}

function removeRow(rowObj) {
    var parent = rowObj.parentNode;
    if (parent.rows.length > 2)
        rowObj.parentNode.removeChild(rowObj);
}

function submitFormTo(formId, formAction) {
    var form = getObj(formId);
    form.action = formAction;
//form.submit();
}

function confirmAction(text) {
    return confirm(text);
}

function highlight(obj, f) {
    if (f == true)
        obj.className = 'highlighted_' + obj.id;
    else
        obj.className = obj.id;
}

function showInfoWin(type, fileId) {
    popUpNoScroll('AdminEntity?type=' + type + '&action=info&id=' + fileId, 'info', 400, 250);
}

function showInfo(id) {
    getObj('showInfo_' + id).style.display = 'none';
    getObj('info_' + id).style.display = '';
}

function hideInfo(id) {
    getObj('showInfo_' + id).style.display = '';
    getObj('info_' + id).style.display = 'none';
}


function editPopUp(url, winName) {
    return popUp(url, winName, 850, 500);
}

function previewPopUp(url, winName) {
    // alert(winName);
    return popUp(url, winName, 900, 700, " ");
}

function popUp(url, winName, w, h) {
    day = new Date();
    id = day.getTime();
    if (isFirefoxOnWindows()) {
        x = window.screenX;
        y = window.screenY;
    } else {
        x = window.screenLeft;
        y = window.screenTop;

    }
    if (w == 850 && h == 500) {
        w = w + 100;
        h = h + 100;
    }
    var plug = false;
    if (url.indexOf("view") == -1) {

        win = window.open(url, winName, 'left=' + x + ',top=' + y + ',width=' + w + ',height=' + h + ',resizable=yes,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=yes');
    } else {
        win = window.open(url, winName, 'left=' + x + ',top=' + y + ',width=' + w + ',height=' + h + ',resizable=yes,toolbar=yes,location=no,directories=no,status=no,menubar=no,scrollbars=yes');

    }
    if (win) {
        win.focus();
    }
    else {
        var timer = win.setTimeout(function() {
            if (win)
                win.focus();
        }, 100);
    }

    return w;
}

function popUpWithScroll(url, winName, w, h) {
    day = new Date();
    id = day.getTime();
    x = window.screenLeft;
    y = window.screenTop;
    w = window.open(url, winName, 'left=' + x + ',top=' + y + ',width=' + w + ',height=' + h + ',resizable=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no');
    w.focus();
    return w;
}

function popUpNoScroll(url, winName, w, h) {
    day = new Date();
    id = day.getTime();
    x = window.screenLeft;
    y = window.screenTop;
    w = window.open(url, winName, 'left=' + x + ',top=' + y + ',width=' + w + ',height=' + h + ',resizable=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no');
    w.focus();
    return w;
}


//Variable to check if link is clicked!
var linkClicked = false;

function closeCardAndMakeItAvailable(filename) {


    //alert(linkClicked);
    if (linkClicked) {
        //alert("ITS A LINK");
    } else {
        //alert("CLOSING");
        popUp("SystemMessages?file=" + filename + "&time=3000", "lala", "300", "150", " ")
    }

}


function onChangeField(sel, id, oper) {
    oper.selectedIndex = id;
}

function onChangeField2(sel, id, oper) {

    oper.selectedIndex = id;

    document.getElementById("dataTypes").selectedIndex = id;
    var dataType = document.getElementById("dataTypes").value;

    if (dataType == "string") {
        sel.parentNode.parentNode.cells[2].childNodes[1].disabled = true;
        sel.parentNode.parentNode.cells[2].childNodes[0].disabled = false;
        sel.parentNode.parentNode.cells[2].childNodes[0].style.display = 'block';
        sel.parentNode.parentNode.cells[2].childNodes[1].style.display = 'none';
        sel.parentNode.parentNode.cells[3].getElementsByTagName("div")[0].childNodes[1].disabled = true;
        sel.parentNode.parentNode.cells[3].getElementsByTagName("div")[0].style.display = 'none';
        sel.parentNode.parentNode.cells[3].childNodes[0].disabled = false;
        sel.parentNode.parentNode.cells[3].childNodes[0].style.display = 'block';
    } else if (dataType == "time") {
        sel.parentNode.parentNode.cells[2].childNodes[0].disabled = true;
        sel.parentNode.parentNode.cells[2].childNodes[1].disabled = false;
        sel.parentNode.parentNode.cells[2].childNodes[1].style.display = 'block';
        sel.parentNode.parentNode.cells[2].childNodes[0].style.display = 'none';
        sel.parentNode.parentNode.cells[3].childNodes[0].disabled = true;
        sel.parentNode.parentNode.cells[3].childNodes[0].style.display = 'none';
        sel.parentNode.parentNode.cells[3].getElementsByTagName("div")[0].style.display = 'block';
        sel.parentNode.parentNode.cells[3].getElementsByTagName("div")[0].childNodes[1].disabled = false;
        sel.parentNode.parentNode.cells[3].getElementsByTagName("div")[0].disabled = false;
    }
}

function timeCheck(timeValue) {

    var str = timeValue.value;
    var Patterns = createPatternsTable(LanguageOfManual);

    for (i = 0; i < Patterns.length; i++) {
        var reg = new RegExp(Patterns[i]);
        if (reg.test(str)) {
            timeValue.style.color = "#000000";
            break;
        } else {
            timeValue.style.color = "#FF0000";
        }
    }
}

function createPatternsTable(language) {

    if (language == "gr") {

        var MonthsPat = "(\\s([ιΙ][αΑ][νΝ][οΟ][υΥ][αάΑ][ρΡ][ιΙ][οΟ][ςΣ]|[φΦ][εΕ][βΒ][ρΡ][οΟ][υΥ][αάΑ][ρΡ][ιΙ][οΟ][ςΣ]|[μΜ][αάΑ][ρΡ][τΤ][ιΙ][οΟ][ςΣ]|[αΑ][πΠ][ρΡ][ιίΙ][λΛ][ιΙ][οΟ][ςΣ]|[μΜ][αάΑ][ιΙϊ][οΟ][ςΣ]|[ιΙ][οΟ][υύΥ][νΝ][ιΙ][οΟ][ςΣ]|[ιΙ][οΟ][υύΥ][λΛ][ιΙ][οΟ][ςΣ]|[αΑ][υύΥ][γΓ][οΟ][υΥ][σΣ][τΤ][οΟ][ςΣ]|[σΣ][εΕ][πΠ][τΤ][εέΕ][μΜ][βΒ][ρΡ][ιΙ][οΟ][ςΣ]|[οΟ][κΚ][τΤ][ωώΩ][βΒ][ρΡ][ιΙ][οΟ][ςΣ]|[νΝ][οΟ][εέΕ][μΜ][βΒ][ρΡ][ιΙ][οΟ][ςΣ]|[δΔ][εΕ][κΚ][εέΕ][μΜ][βΒ][ρΡ][ιΙ][οΟ][ςΣ]))";

        var BChristPat = "(\\s[πΠ]\\.[χΧ]\\.)";
        var AChristPat = "(\\s[μΜ]\\.[χΧ]\\.)";
        var DecadeNoPat = "([πΠ][ρΡ][ωώΩ][τΤ][ηΗ]|[δΔ][εΕ][υύΥ][τΤ][εΕ][ρΡ][ηΗ]|[τΤ][ρΡ][ιίΙ][τΤ][ηΗ]|[τΤ][εέΕ][τΤ][αΑ][ρΡ][τΤ][ηΗ]|[πΠ][εέΕ][μΜ][πΠ][τΤ][ηΗ]|[εέΕ][κΚ][τΤ][ηΗ]|[εέΕ][βΒ][δΔ][οΟ][μΜ][ηΗ]|[οόΟ][γΓ][δΔ][οΟ][ηΗ]|[εέΕ][νΝ][αΑ][τΤ][ηΗ]|[δΔ][εέΕ][κΚ][αΑ][τΤ][ηΗ]|[τΤ][εΕ][λΛ][εΕ][υΥ][τΤ][αΑ][ιίΙ][αΑ])";

        var DecadePat = "[δΔ][εΕ][κΚ][αΑ][εΕ][τΤ][ιίΙ][αΑ]";
        var OfPat = "[τΤ][οΟ][υΥ]";
        var OuPat = "ου";
        var OsPat = "ος"
        var CenturyPat = "[αΑ][ιΙ][ωώΩ][νΝ][αΑ]";
        var Century2Pat = "[αΑ][ιΙ][ωώΩ][νΝ][αΑ][ςΣ]";
        var CenturyPart1Pat = "[αΑ][ρΡ][χΧ][εέΕ][ςΣ]|[μΜ][εέΕ][σΣ][αΑ]|[τΤ][εέΕ][λΛ][ηΗ]";
        var HalfPat = "[μΜ][ιΙ][σΣ][οόΟ]";
        var QuarterPat = "[τΤ][εέΕ][τΤ][αΑ][ρΡ][τΤ][οΟ]";
        var HalfNoPat = "(α|β)'";
        var QuarterNoPat = "(α|β|γ|δ)'";
    } else if (language == "en") {
        var MonthsPat = "(\\s([jJ][aA][nN][uU][aA][rR][yY]|[fF][eE][bB][rR][uU][aA][rR][yY]|[mM][aA][rR][cC][hH]|[aA][pP][rR][iI][lL]|[mM][aA][yY]|[jJ][uU][nN][eE]|[jJ][uU][lL][yY]|[aA][uU][gG][uU][sS][tT]|[sS][eE][pP][tT][eE][mM][bB][eE][rR]|[oO][cC][tT][oO][bB][eE][rR]|[nN][oO][vV][eE][mM][bB][eE][rR]|[dD][eE][cC][eE][mM][bB][eE][rR]))";
        var BChristPat = "(\\s[bB][cC][eE])";
        var AChristPat = "(\\s[cC][eE])";
        var DecadeNoPat = "([fF][iI][rR][sS][tT]|[sS][eE][cC][oO][nN][dD]|[tT][hH][iI][rR][dD]|[fF][oO][uU][rR][tT][hH]|[fF][iI][fF][tT][hH]|[sS][iI][xX][tT][hH]|[sS][eE][vV][eE][nN][tT][hH]|[eE][iI][gG][hH][tT][hH]|[nN][iI][nN][tT][hH]|[tT][eE][nN][tT][hH] |[lL][aA][sS][tT])";
        var DecadePat = "[dD][eE][cC][aA][dD][eE]";
        var OfPat = "[oO][fF]";
        var OuPat = "th|[1]st|[2]nd|3rd";
        var OsPat = "th|[1]st|[2]nd|3rd"
        var CenturyPat = "[cC][eE][nN][tT][uU][rR][yY]";
        var Century2Pat = "[cC][eE][nN][tT][uU][rR][yY]";
        var CenturyPart1Pat = "[eE][aA][rR][lL][yY]|[mM][iI][dD]|[lL][aA][tT][eE]";
        var HalfPat = "[hH][aA][lL][fF]";
        var QuarterPat = "[qQ][uU][aA][rR][tT][eE][rR]";
        var HalfNoPat = "(1st|2nd)";
        var QuarterNoPat = "(1st|2nd|3rd|4th)";
    }
    //ui patterns
    var PatternBCFullEnd = BChristPat + "?$";
    var PatternACFullEnd = AChristPat + "$";
    var Pavla = "\\s+-\\s+";
    //No Christ Patterns-Basic combos
    var Pattern1 = "\\d{1,4}?" + MonthsPat + "?(\\s([12]\\d|3[01]|[1-9]))?";
    var Pattern2 = "(" + DecadeNoPat + "+\\s)?" + DecadePat + "\\s" + OfPat + " ((\\d{1,3}" + OuPat + ")\\s" + CenturyPat + "|\\d{1,4})";
    var Pattern3 = "(\\d{1,3}" + OsPat + ")\\s" + Century2Pat;
    var Pattern4 = "(" + CenturyPart1Pat + "|" + HalfNoPat + "\\s" + HalfPat + "|" + QuarterNoPat + "\\s" + QuarterPat + ")\\s(\\d{1,3}" + OuPat + ")\\s" + CenturyPat;
    var Pattern5 = "\\d{1,4}\\s,\\sca\\.";

    var Patterns = new Array()
    Patterns[0] = "^\\s*$";
    Patterns[1] = "^" + Pattern1 + PatternBCFullEnd;
    Patterns[2] = "^" + Pattern2 + PatternBCFullEnd;
    Patterns[3] = "^" + Pattern3 + PatternBCFullEnd;
    Patterns[4] = "^" + Pattern1 + Pavla + Pattern1 + PatternBCFullEnd;
    Patterns[5] = "^" + Pattern1 + Pavla + Pattern2 + PatternBCFullEnd;
    Patterns[6] = "^" + Pattern1 + Pavla + Pattern3 + PatternBCFullEnd;
    Patterns[7] = "^" + Pattern2 + Pavla + Pattern1 + PatternBCFullEnd;
    Patterns[8] = "^" + Pattern2 + Pavla + Pattern2 + PatternBCFullEnd;
    Patterns[9] = "^" + Pattern2 + Pavla + Pattern3 + PatternBCFullEnd;
    Patterns[10] = "^" + Pattern3 + Pavla + Pattern1 + PatternBCFullEnd;
    Patterns[11] = "^" + Pattern3 + Pavla + Pattern2 + PatternBCFullEnd;
    Patterns[12] = "^" + Pattern3 + Pavla + Pattern3 + PatternBCFullEnd;
    Patterns[13] = "^" + Pattern4 + PatternBCFullEnd;
    Patterns[14] = "^" + Pattern5 + PatternBCFullEnd;
    Patterns[15] = "^" + Pattern1 + BChristPat + Pavla + Pattern1 + AChristPat + "$";
    Patterns[16] = "^" + Pattern1 + BChristPat + Pavla + Pattern2 + AChristPat + "$";
    Patterns[17] = "^" + Pattern1 + BChristPat + Pavla + Pattern3 + AChristPat + "$";
    Patterns[18] = "^" + Pattern1 + BChristPat + Pavla + Pattern4 + AChristPat + "$";
    Patterns[19] = "^" + Pattern1 + BChristPat + Pavla + Pattern5 + AChristPat + "$";
    Patterns[20] = "^" + Pattern2 + BChristPat + Pavla + Pattern1 + AChristPat + "$";
    Patterns[21] = "^" + Pattern2 + BChristPat + Pavla + Pattern2 + AChristPat + "$";
    Patterns[22] = "^" + Pattern2 + BChristPat + Pavla + Pattern3 + AChristPat + "$";
    Patterns[23] = "^" + Pattern2 + BChristPat + Pavla + Pattern4 + AChristPat + "$";
    Patterns[24] = "^" + Pattern2 + BChristPat + Pavla + Pattern5 + AChristPat + "$";
    Patterns[25] = "^" + Pattern3 + BChristPat + Pavla + Pattern1 + AChristPat + "$";
    Patterns[26] = "^" + Pattern3 + BChristPat + Pavla + Pattern2 + AChristPat + "$";
    Patterns[27] = "^" + Pattern3 + BChristPat + Pavla + Pattern3 + AChristPat + "$";
    Patterns[28] = "^" + Pattern3 + BChristPat + Pavla + Pattern4 + AChristPat + "$";
    Patterns[29] = "^" + Pattern3 + BChristPat + Pavla + Pattern5 + AChristPat + "$";
    Patterns[30] = "^" + Pattern4 + BChristPat + Pavla + Pattern1 + AChristPat + "$";
    Patterns[31] = "^" + Pattern4 + BChristPat + Pavla + Pattern2 + AChristPat + "$";
    Patterns[32] = "^" + Pattern4 + BChristPat + Pavla + Pattern3 + AChristPat + "$";
    Patterns[33] = "^" + Pattern4 + BChristPat + Pavla + Pattern4 + AChristPat + "$";
    Patterns[34] = "^" + Pattern4 + BChristPat + Pavla + Pattern5 + AChristPat + "$";
    Patterns[35] = "^" + Pattern5 + BChristPat + Pavla + Pattern1 + AChristPat + "$";
    Patterns[36] = "^" + Pattern5 + BChristPat + Pavla + Pattern2 + AChristPat + "$";
    Patterns[37] = "^" + Pattern5 + BChristPat + Pavla + Pattern3 + AChristPat + "$";
    Patterns[38] = "^" + Pattern5 + BChristPat + Pavla + Pattern4 + AChristPat + "$";
    Patterns[39] = "^" + Pattern5 + BChristPat + Pavla + Pattern5 + AChristPat + "$";

    return Patterns;
}

function dosubmit(formName, ServletName) {

    var frm = document.forms[formName];
    frm.method = "post";
    frm.action = ServletName;

    if (ServletName == 'ViewAll') {
        frm.target = "_blank";
    } else if (ServletName == 'Search' && formName == 'BugReportResultsFrm') {
        frm.target = "_self";
    }
    frm.submit();
}

function setValue(chechBox) {
    if (chechBox.checked == true)
        chechBox.value = 'checked';
    else
        chechBox.value = '';
}


function checkUncheckAll(theElement) {
    var theForm = theElement.form, z = 0;
    for (z = 0; z < theForm.length; z++) {
        if (theForm[z].type == 'checkbox' && theForm[z].name != 'SelectAll') {
            theForm[z].checked = theElement.checked;
            if (theForm[z].checked == true)
                theForm[z].value = 'checked';
            else
                theForm[z].value = '';
        }
    }

}

function countChecked(results) {
    var counter = 0;
    for (var x = 1; x <= results; x++)
    {
        if (document.getElementById("checkbox" + x).getAttribute("value") == 'checked')
        {
            counter++;
        }
    }
    return counter;
}

function disp_confirm(msgOne, msgMore, msgNo, image, count, formName, EntityType) {

    flag = false;
    if (countChecked(count) == 1) {
        for (var x = 1; x <= count; x++) {

            if (document.getElementById("checkbox" + x).getAttribute("value") == 'checked')
            {
                break;
            }
        }
        var id = document.getElementById("checkbox" + x).getAttribute("name");
        var r = confirm(msgOne + id.split("Selectcheckbox")[1] + "?");
    } else if (countChecked(count) > 1) {
        var r = confirm(msgMore + countChecked(count) + image);
    } else {
        var r = alert(msgNo);
        flag = true;
    }

    if (r == true && flag != true)
    {
        for (var x = 1; x <= count; x++) {

            if (document.getElementById("checkbox" + x).getAttribute("value") == 'checked') {
                var id = document.getElementById("checkbox" + x).getAttribute("name");
                location.href = "DeleteEntity?type=" + EntityType + "&id=" + id.split("Selectcheckbox")[1];
            }
        }
    }
}

function docancel(formName, ServletName) {

    var frm = document.forms[formName];

    frm.method = "post";
    frm.action = ServletName;

    if (ServletName == 'ViewAll') {
        frm.target = "_blank";
    } else if (ServletName == 'Cancel' && formName == 'BugReportResultsFrm') {
        frm.target = "_self";
    }
    frm.cancel();
}

function confirmActionDel(text, id) {
    return confirm(text + id + "?");
}

function confirmActionRestore(text, date, time) {
    return confirm(text + date + "[ " + time + " ]");
}

var javascriptVersion1_1 = false;
var detectableWithVB = false;
var pluginFound = false;


function goURL(daURL) {
    // if the browser can do it, use replace to preserve back button
    if (javascriptVersion1_1) {
        window.location.replace(daURL);
    } else {
        window.location = daURL;
    }
    return;
}

function redirectCheck(pluginFound, redirectURL, redirectIfFound) {
    // check for redirection
    if (redirectURL && ((pluginFound && redirectIfFound) ||
            (!pluginFound && !redirectIfFound))) {
        // go away
        goURL(redirectURL);
        return pluginFound;
    } else {
        // stay here and return result of plugin detection
        return pluginFound;
    }
}


// Here we write out the VBScript block for MSIE Windows

if ((navigator.userAgent.indexOf('MSIE') != -1) && (navigator.userAgent.indexOf('Win') != -1)) {
    document.writeln('<script language="VBscript">');
    document.writeln('\'do a one-time test for a version of VBScript that can handle this code');
    document.writeln('detectableWithVB = False');
    document.writeln('If ScriptEngineMajorVersion >= 2 then');
    document.writeln('  detectableWithVB = True');
    document.writeln('End If');
    document.writeln('\'this next function will detect most plugins');
    document.writeln('Function detectActiveXControl(activeXControlName)');
    document.writeln('  on error resume next');
    document.writeln('  detectActiveXControl = False');
    document.writeln('  If detectableWithVB Then');
    document.writeln('     detectActiveXControl = IsObject(CreateObject(activeXControlName))');
    document.writeln('  End If');
    document.writeln('End Function');
    document.writeln('</scr' + 'ipt>');
}



function refreshUploader(who) {
    // pass who as argument,
    who = document.getElementsByName('xx_file')[0];
    var who2 = who.cloneNode(false);
    who2.onchange = who.onchange;// events are not cloned
    who.parentNode.replaceChild(who2, who);

}


function selectFolder(pathName) {

    var frm = document.forms["MassImportForm"];
    var bn = pathName.substring(0, pathName.lastIndexOf('\\'));
    who = document.getElementsByName('Topo8esiaFoto')[0];
    var who2 = who.cloneNode(false);
    who2.onchange = who.onchange;// events are not cloned
    who2.appendChild(bn);
    who.parentNode.replaceChild(who2, who);
}



function validateForm(MasterFile, Master, ExcelMissing) {

    var frm = document.forms["MassImportForm"];

    if (frm.space.value == "false") {
        alert(nofreespace);
        return false;
    } else if (frm.foto.value == "") {
        alert(Master);
        frm.foto.focus();
        return false;
    } else if (frm.excel.value == "") {
        alert(ExcelMissing);
        frm.excel.focus();
        return false;
    } else {
        frm.fotoFilename.value = frm.foto.value;
        var bn = frm.fotoFilename.value;
        bn = bn.substring(0, bn.lastIndexOf('\\'));
        frm.fotoFilename.value = bn;
        frm.excelFilename.value = frm.excel.value;
        var folder = bn.substring(bn.lastIndexOf('\\') + 1);
        if (folder == "Master") {
            alert(MasterFile);
            return true;
        } else {
            alert(Master);
            frm.foto.focus();
            return false;
        }
    }
}

//mdaskal
function confirmActionRestore(text, date, time) {
    return confirm(text + date + "[ " + time + " ]");
}


function isFirefoxOnWindows() {
    return ((navigator.userAgent.indexOf("Firefox") != -1) && (navigator.userAgent.indexOf("Win") != -1));
}

// return true if the page loads in Internet Explorer
function isIEOnWindows() {
    return ((navigator.userAgent.indexOf('MSIE') != -1) && (navigator.userAgent.indexOf('Win') != -1))
}

//return true if Browser is 64bit
function is64bitBrowser() {
    return ((navigator.userAgent.indexOf('Win64') != -1) && (navigator.userAgent.indexOf('x64') != -1))
}

function replaceAll(txt, replace, with_this) {
    return txt.replace(new RegExp(replace, 'g'), with_this);
}


function writeDependencies(title, content) {

    content = replaceAll(content, "\t", "<br/>");

    var ref = window.open('', title,
            'width=350,height=250'
            + ',menubar=0'
            + ',toolbar=1'
            + ',status=0'
            + ',scrollbars=1'
            + ',resizable=1')
    ref.document.writeln(
            '<html><head><title>' + title + '</title></head>'
            + '<body bgcolor=white onLoad="self.focus()">'
            + '<h3>' + title + ':' + '</h3>'
            + content
            + '</body></html>'
            )
    ref.document.close()
}


function toggleVisibility(id) {
    if (id.style.display == 'block')
        id.style.display = 'none';
    else
        id.style.display = 'block';
}


var timeout = 10;
var closetimer = 0;
var ddmenuitem = 0;

// open hidden layer
function mopen(id) {
    // cancel close timer
    mcancelclosetime();

    // close old layer
    if (ddmenuitem)
        ddmenuitem.style.visibility = 'hidden';

    // get new layer and show it
    ddmenuitem = document.getElementById(id);
    ddmenuitem.style.visibility = 'visible';

}
// close showed layer
function mclose()
{
    if (ddmenuitem)
        ddmenuitem.style.visibility = 'hidden';
}

// go close timer
function mclosetime()
{
    closetimer = window.setTimeout(mclose, timeout);
}

// cancel close timer
function mcancelclosetime()
{
    if (closetimer)
    {
        window.clearTimeout(closetimer);
        closetimer = null;
    }
}

// close layer when click-out
document.onclick = mclose;

var popupWindow = null;
function centeredPopup(url, winName, w, h, scroll) {
    LeftPosition = (screen.width) ? (screen.width - w) / 2 : 0;
    TopPosition = (screen.height) ? (screen.height - h) / 2 : 0;
    settings =
            'height=' + h + ',width=' + w + ',top=' + TopPosition + ',left=' + LeftPosition + ',scrollbars=' + scroll + ',resizable'
    popupWindow = window.open(url, winName, settings)
}
function getElementsByClassName(node, classname) {
    if (node.getElementsByClassName) { // use native implementation if available
        return node.getElementsByClassName(classname);
    } else {
        return (function getElementsByClass(searchClass, node) {
            if (node == null)
                node = document;
            var classElements = [],
                    els = node.getElementsByTagName("*"),
                    elsLen = els.length,
                    pattern = new RegExp("(^|\\s)" + searchClass + "(\\s|$)"), i, j;

            for (i = 0, j = 0; i < elsLen; i++) {
                if (pattern.test(els[i].className)) {
                    classElements[j] = els[i];
                    j++;
                }
            }
            return classElements;
        })(classname, node);
    }
}

function toggle_visibility_Class(className, display) {
    var elements = getElementsByClassName(document, className),
            n = elements.length;
    for (var i = 0; i < n; i++) {
        var e = elements[i];

        if (display.length > 0) {
            e.style.display = display;
        } else {
            if (e.style.display == 'block') {
                e.style.display = 'none';
            } else {
                e.style.display = 'block';
            }
        }
    }
}