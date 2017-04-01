var langucmfriendsresource = {
shareuri:'分享ID',
fileid:'文件ID',
filename:'文件名',
filesize:'文件大小',
fileext:'文件EXT',
filetype:'文件类型',
createtime:'创建时间',
modifytime:'修改时间',
location:'文件位置',
extend1:'备用字段1',
extend2:'备用字段2'};
var UcmFriendsResourceControl = function(_compId){
var gridStart = 0;
var gridLimit = 10;
var ucmFriendsResourceGridDataSource;
var ucmFriendsResourceGridSearchUrl = 'ucmFriendsResourceData.jsp';
var ucmFriendsResourceGridDeleteUrl = 'ucmFriendsResourceData.jsp';
var ucmFriendsResourceFormSaveUrl = 'ucmFriendsResourceData.jsp?action=save';
var ucmFriendsResourceFormLoadUrl = 'ucmFriendsResourceData.jsp?action=getObject';


var theUploadFileUrl = 'uploadFile.do';
var theDownLoadUrl = 'downloadFile.do';
//GRID列
var ucmFriendsResourceSmMain = new Ext.grid.CheckboxSelectionModel();

//列对应了下面的json数据,列中表示的index必须与下面的ucmFriendsResourceGridDataSource对应上。
var ucmFriendsResourceCmMain;

var shareUriSField;


var ucmFriendsResourceMainGrid;

//
function initGridColumn() {

ucmFriendsResourceCmMain = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), ucmFriendsResourceSmMain,
{
    header: lang('langucmfriendsresource.shareuri'),
    dataIndex: 'shareUri',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.fileid'),
    dataIndex: 'fileId',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.filename'),
    dataIndex: 'fileName',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.filesize'),
    dataIndex: 'fileSize',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.fileext'),
    dataIndex: 'fileExt',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.filetype'),
    dataIndex: 'fileType',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.createtime'),
    dataIndex: 'createtime',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.modifytime'),
    dataIndex: 'modifytime',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.location'),
    dataIndex: 'location',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.extend1'),
    dataIndex: 'extend1',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsresource.extend2'),
    dataIndex: 'extend2',
    sortable: false
}




]);

//JSON数据,从后台获取的JSON数据映射到本地的Grid中
//proxy直接去读取josn数据
ucmFriendsResourceGridDataSource = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: ucmFriendsResourceGridSearchUrl
    }),
    reader: new Ext.data.JsonReader({
        totalProperty: 'totalProperty',
        root: 'root',
        successProperty: '@success'
    }, [
 
    {
	    name: 'uri',
	    mapping: 'uri',
	    type: 'string'
    }   
,
    {
        name: 'shareUri',
        mapping: 'shareUri',
        type: 'string'
    }

,
    {
        name: 'fileId',
        mapping: 'fileId',
        type: 'string'
    }

,
    {
        name: 'fileName',
        mapping: 'fileName',
        type: 'string'
    }

,
    {
        name: 'fileSize',
        mapping: 'fileSize',
        type: 'string'
    }

,
    {
        name: 'fileExt',
        mapping: 'fileExt',
        type: 'string'
    }

,
    {
        name: 'fileType',
        mapping: 'fileType',
        type: 'string'
    }

,
    {
        name: 'createtime',
        mapping: 'createtime',
        type: 'string'
    }

,
    {
        name: 'modifytime',
        mapping: 'modifytime',
        type: 'string'
    }

,
    {
        name: 'location',
        mapping: 'location',
        type: 'string'
    }

,
    {
        name: 'extend1',
        mapping: 'extend1',
        type: 'string'
    }

,
    {
        name: 'extend2',
        mapping: 'extend2',
        type: 'string'
    }

    
    
    ])
});

ucmFriendsResourceGridDataSource.on('beforeload', function(thisDs, options){
    thisDs.baseParams = getTheSearchParam(false);
});

} //end initColumn() function


function initSearchFieldData() {
	var now = new Date();
	var startDate = new Date(now.getTime()-30*24*60*60*1000);
	var endDate = new Date(now.getTime()+24*60*60*1000);
			shareUriSField.setValue('');
		
	}

function getTheSearchParam(_page) {
var shareUriSValue = shareUriSField.getValue();





var theParams = {
action: 'search'
,
shareUri : shareUriSValue

};
		
if (_page == true) {
	theParams.start = gridStart;
	theParams.limit = gridLimit;
}

return theParams;
}


function initGridSearch() {
shareUriSField = new Ext.form.TextField({
    width: 100,
    labelWidth: 80,
    name: "shareUri"
    /*,id: "sshareUri"*/
});


initSearchFieldData();

} //end initGridSearch() function


function initGridMain() {

var theGridId = _compId;
if(theGridId == undefined || theGridId == null) {
	theGridId = "none";
}
		
//显示网格的组件
ucmFriendsResourceMainGrid = new Ext.grid.GridPanel({
    /*el: 'ucmFriendsResourceMainGrid',*/
    loadMask: theMask,
    ds: ucmFriendsResourceGridDataSource,
    sm: ucmFriendsResourceSmMain,
    cm: ucmFriendsResourceCmMain,
    autoWidth: true,
	height: 550,
    tbar: [
lang('langucmfriendsresource.shareuri'), shareUriSField, 

    	{
        text: lang('l_search'),
        handler: ucmFriendsResourceSearchForm
    },{
		text : '重置',
		handler : initSearchFieldData
	}],
    bbar: new Ext.PagingToolbar({
        pageSize: gridLimit,
        store: ucmFriendsResourceGridDataSource,
        displayInfo: true,
        displayMsg: lang('l_page_displaymsg'),
        emptyMsg: lang('l_page_emptymsg')
    }),
    buttonAlign: 'left',
    buttons: [{
        text: lang('l_add'),
        handler: function(){
            if (ucmFriendsResourceMainFormWin.isVisible()) {
                ucmFriendsResourceMainFormWin.hide();
            }
            else {
                ucmFriendsResourceMainFormWin.show();
                ucmFriendsResourceMainForm.getForm().reset();
            }
            
            ucmFriendsResourceFormChange('add',ucmFriendsResourceMainForm,ucmFriendsResourceMainFormWin);
        }
    }, {
        text: lang('l_view'),
        handler: function(){
            var selectedData = ucmFriendsResourceSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsResourceMainForm.form.load({
                    url: ucmFriendsResourceFormLoadUrl,
                    waitMsg: lang('l_waiting'),
                    failure: function(){
                        //debug('failure');
                    },
                    success: function(){
                        //debug('success');
                        
                        //debug(selectedData.data.uri);
                        
                    },
                    params: {
                    	uri: selectedData.data.uri
                    }
                    
                });
				
				ucmFriendsResourceFormChange('view',ucmFriendsResourceMainForm,ucmFriendsResourceMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsResourceAlertResult);
            }
        }
        
    }, {
        text: lang('l_edit'),
        handler: function(){
            var selectedData = ucmFriendsResourceSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsResourceMainForm.form.load({
                    url: ucmFriendsResourceFormLoadUrl,
                    waitMsg: lang('l_waiting'),
                    failure: function(){
                        //debug('failure');
                    },
                    success: function(){
                        //debug('success');
                        
                        //debug(selectedData.data.uri);
                        
                    },
                    params: {
                    	uri: selectedData.data.uri
                    }
                    
                });
				
				ucmFriendsResourceFormChange('edit',ucmFriendsResourceMainForm,ucmFriendsResourceMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsResourceAlertResult);
            }
        }
        
    }, {
        text: lang('l_delete'),
        handler: function(){
            var selectData = ucmFriendsResourceSmMain.getSelections();
            var selectDataStr = "";
            for (var i = 0; i < selectData.length; i++) {
                selectDataStr += selectData[i].data.uri + ",";
            }
            if (selectDataStr != '') {
                //debug('delete:' + selectDataStr);
                Ext.MessageBox.confirm(lang('l_confirm'), lang('l_confirm_delete'), function(confBtn){
                	if(confBtn == 'yes') {
                		Ext.Ajax.request({
                    		url: ucmFriendsResourceGridDeleteUrl,
                    		success: ucmFriendsResourceSearchForm,
                    		failure: ucmFriendsResourceSearchForm,
                    		headers: {
                        		'my-header': 'foo'
                    		},
                    		params: {
                        		action: "delete",
                        		uris: selectDataStr
                    		}
                		});
                	} else {
                		//alert('no');
                	}
                
                });
            }
            else {
                Ext.MessageBox.alert('warn', 'warn delete msg', ucmFriendsResourceAlertResult);
            }
        }
        
    }]
});

} //end initGridMain() function

//
//end init var

function initRelaGrid() {


} //end initRelaGrid() function

//


//end init var


function initUploadFile() {

}

//


var ucmFriendsResourceFormReader;
//value,display,desc
var ucmFriendsResourceFormFields;
var ucmFriendsResourceMainForm;
var ucmFriendsResourceMainFormWin;

function initGridForm() {

ucmFriendsResourceFormReader = new Ext.data.JsonReader({
    root: 'root',
    totalProperty: 'totalProperty',
    successProperty: '@success'
}, [
{
	name: 'uri',
	mapping: 'uri',
	type: 'string'
} 
,
{
        name: 'shareUri',
        mapping: 'shareUri',
        type: 'string'
}

,
{
        name: 'fileId',
        mapping: 'fileId',
        type: 'string'
}

,
{
        name: 'fileName',
        mapping: 'fileName',
        type: 'string'
}

,
{
        name: 'fileSize',
        mapping: 'fileSize',
        type: 'string'
}

,
{
        name: 'fileExt',
        mapping: 'fileExt',
        type: 'string'
}

,
{
        name: 'fileType',
        mapping: 'fileType',
        type: 'string'
}

,
{
        name: 'createtime',
        mapping: 'createtime',
        type: 'string'
}

,
{
        name: 'modifytime',
        mapping: 'modifytime',
        type: 'string'
}

,
{
        name: 'location',
        mapping: 'location',
        type: 'string'
}

,
{
        name: 'extend1',
        mapping: 'extend1',
        type: 'string'
}

,
{
        name: 'extend2',
        mapping: 'extend2',
        type: 'string'
}


]);


ucmFriendsResourceFormFields = [
        new Ext.form.Hidden({
            name: 'uri'
            /*,id: 'uri'*/
        })
,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.shareuri'),
            allowBlank: false,
            name: 'shareUri'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.fileid'),
            allowBlank: false,
            name: 'fileId'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.filename'),
            allowBlank: false,
            name: 'fileName'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.filesize'),
            allowBlank: false,
            name: 'fileSize'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.fileext'),
            allowBlank: false,
            name: 'fileExt'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.filetype'),
            allowBlank: false,
            name: 'fileType'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.createtime'),
            allowBlank: false,
            name: 'createtime'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.modifytime'),
            allowBlank: false,
            name: 'modifytime'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.location'),
            allowBlank: false,
            name: 'location'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.extend1'),
            allowBlank: false,
            name: 'extend1'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsresource.extend2'),
            allowBlank: false,
            name: 'extend2'
        }



        ]
        ;


//增加、修改表单
ucmFriendsResourceMainForm = new Ext.FormPanel({
    /*title: lang('l_ucmFriendsResource_form'),*/
    /*frame: true,*/
    border: false,
	bodyBorder: false,
    labelWidth: 110,
    bodyStyle: 'padding:0 10px 0;',
    reader: ucmFriendsResourceFormReader,
    items: ucmFriendsResourceFormFields,
    buttonAlign: 'left',
    buttons: [{
        //保存操作
        text: lang('l_save'),
        /*id: 'btnsave',*/
        handler: function(){
            
            
            
            ucmFriendsResourceMainForm.form.doAction('submit', { //将表单数据提交到服务器
                url: ucmFriendsResourceFormSaveUrl,
                method: 'post',
                success: function(form, action){
                    ucmFriendsResourceSearchForm();
                    Ext.MessageBox.alert(lang('l_info'), lang('l_message_success'), function(btn){
                    ucmFriendsResourceMainFormWin.hide();
                    
                    });
                },
                failure: function(form, action){
                	var responseRs = action.response.responseText;
                    Ext.MessageBox.alert(lang('l_error'), responseRs);
                    //ucmFriendsResourceMainFormWin.hide();
                }
            });
            
            
        }
    }, {
        //取消操作
        text: lang('l_cancel'),
        handler: function(){
            ucmFriendsResourceMainFormWin.hide();
            
        }
    }]
});


ucmFriendsResourceMainFormWin = new Ext.Window({
	modal: true,
	authWidth: true,
	height: 500,
	width: 550,
	closeAction: 'hide',
	plain: true,
	maximizable: true,
	resizable: true,
	autoScroll: true, 
	//layout: 'fit',
	items: ucmFriendsResourceMainForm
});

} //end initGridForm() function

function ucmFriendsResourceSearchForm(){
ucmFriendsResourceGridDataSource.load({
		params: getTheSearchParam(true)
	}
);
}

function ucmFriendsResourceFormChange(_action,_theForm, _theFormWin) {
	var formBtns = _theForm.buttons;
	if(formBtns.length > 0) {
		for(var i = 0; i<formBtns.length; i++) {
			if("btnsave" == formBtns[i].id) {
				if("view" == _action) {
					formBtns[i].hide();
					break;	
				} else {
					formBtns[i].show();
					break;	
				}
			}
		}
	}
	
	if("add" == _action) {
	} else {
		_theForm.show();
		if(_theFormWin != null) {
			_theFormWin.show();
		}
	}
}


function ucmFriendsResourceAlertResult(){

}

this.initGrid = function(){
	initGridColumn();
	initGridSearch();
	initGridMain();
	initRelaGrid();
	initUploadFile();
	initGridForm();
	return this.initData();
}

this.initData = function() {
	return ucmFriendsResourceMainGrid;
}

this.searchData = function() {
	ucmFriendsResourceGridDataSource.load({
		params: {
			start: gridStart,
			limit: gridLimit
		}
	});
}

} //end object init
