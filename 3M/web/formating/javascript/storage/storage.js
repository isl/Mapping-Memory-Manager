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
/*fuctions for backup and restore*/
var xmlHttp


function GetXmlHttpObject() {
    var objXMLHttp = null
    if (window.XMLHttpRequest) {
        objXMLHttp = new XMLHttpRequest();
    }
    else if (window.ActiveXObject) {
        objXMLHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
    return objXMLHttp;
}

function DisableEnableLinks(xHow) {
    objLinks = document.links;
    for (i = 0; i < objLinks.length; i++) {
        objLinks[i].disabled = xHow;
        //link with onclick
        if (objLinks[i].onclick && xHow) {
            objLinks[i].onclick = new Function("return false;" + objLinks[i].onclick.toString().getFuncBody());
        }
        //link without onclick
        else if (xHow) {
            objLinks[i].onclick = function() {
                return false;
            }
        }
        //remove return false with link without onclick
        else if (!xHow && objLinks[i].onclick.toString().indexOf("function(){return false;}") != -1) {
            objLinks[i].onclick = null;
        }
        //remove return false link with onclick
        else if (!xHow && objLinks[i].onclick.toString().indexOf("return false;") != -1) {
            strClick = objLinks[i].onclick.toString().getFuncBody().replace("return false;}", "");
            objLinks[i].onclick = new Function("");
        }
    }
}

String.prototype.getFuncBody = function() {
    var str = this.toString();
    str = str.replace(/[^{]+{/, "");
    str = str.substring(0, str.length - 1);
    str = str.replace(/\n/gi, "");
    if (!str.match(/\(.*\)/gi))
        str += ")";
    return str;
}


function backupRestore(u, action, filename, space) {
    //alert(filename)
    if (space == "false" && (action == "backup" || action == "restore"))
        alert(nofreespace);
    else {

        if (action == "backup") {
            var r = confirm(backupMsg);
            if (r == true) {
                document.getElementById("pleasewaitScreenB").style.visibility = "visible";
                DisableEnableLinks(true);
                window.setTimeout("showbackupRestore('" + u + "','" + action + "')", 1);
            }
        }
        else if (action == "restore") {
            document.getElementById("pleasewaitScreenR").style.visibility = "visible";
            //u = u+'&file='+filename;
            DisableEnableLinks(true);
            window.setTimeout("showbackupRestore('" + u + "','" + action + "')", 1);
        }
        else {
            location.href = u;
        }
    }

}

function showbackupRestore(url, action) {

    xmlHttp = GetXmlHttpObject()
    if (xmlHttp == null) {
        //Dead browser
        alert("Browser does not support HTTP Request")
        return
    }

    xmlHttp.onreadystatechange = function() {
        if (xmlHttp.readyState == 4 || xmlHttp.readyState == "complete") {
            //When response ready fill div and display it. Also hide loading div.
            if (action == "backup") {
                document.getElementById("pleasewaitScreenB").style.visibility = "hidden";

                DisableEnableLinks(false);
                //document.getElementById("pleasewaitScreenB").innerHTML=xmlHttp.responseText;
                alert(xmlHttp.responseText);
                url = url.replace(/backup/i, "list") + "&menuId=Backup";
                location.href = url;
            }
            else {
                DisableEnableLinks(false);
                document.getElementById("pleasewaitScreenR").style.visibility = "hidden";
                alert(xmlHttp.responseText);
            }
        }
    }

    xmlHttp.open("POST", url, true)
    xmlHttp.send(null)

}