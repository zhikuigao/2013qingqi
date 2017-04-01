var langucmfriendscover = {
userid:'用户ID',
fileid:'文件ID',
extend1:'备用字段1',
extend2:'备用字段2'};
var UcmFriendsCoverControl = function(_compId){
var gridStart = 0;
var gridLimit = 10;
var ucmFriendsCoverGridDataSource;
var ucmFriendsCoverGridSearchUrl = 'ucmFriendsCoverData.jsp';
var ucmFriendsCoverGridDeleteUrl = 'ucmFriendsCoverData.jsp';
var ucmFriendsCoverFormSaveUrl = 'ucmFriendsCoverData.jsp?action=save';
var ucmFriendsCoverFormLoadUrl = 'ucmFriendsCoverData.jsp?action=getObject';


var theUploadFileUrl = 'uploadFile.do';
var theDownLoadUrl = 'downloadFile.do';
//GRID列
var ucmFriendsCoverSmMain = new Ext.grid.CheckboxSelectionModel();

//列对应了下面的json数据,列中表示的index必须与下面的ucmFriendsCoverGridDataSource对应上。
var ucmFriendsCoverCmMain;

var userIdSField;


var ucmFriendsCoverMainGrid;

//
function initGridColumn() {

ucmFriendsCoverCmMain = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), ucmFriendsCoverSmMain,
{
    header: lang('langucmfriendscover.userid'),
    dataIndex: 'userId',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscover.fileid'),
    dataIndex: 'fileId',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscover.extend1'),
    dataIndex: 'extend1',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscover.extend2'),
    dataIndex: 'extend2',
    sortable: false
}




]);

//JSON数据,从后台获取的JSON数据映射到本地的Grid中
//proxy直接去读取josn数据
ucmFriendsCoverGridDataSource = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: ucmFriendsCoverGridSearchUrl
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
        name: 'userId',
        mapping: 'userId',
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

ucmFriendsCoverGridDataSource.on('beforeload', function(thisDs, options){
    thisDs.baseParams = getTheSearchParam(false);
});

} //end initColumn() function


function initSearchFieldData() {
	var now = new Date();
	var startDate = new Date(now.getTime()-30*24*60*60*1000);
	var endDate = new Date(now.getTime()+24*60*60*1000);
			userIdSField.setValue('');
		
	}

function getTheSearchParam(_page) {
var userIdSValue = userIdSField.getValue();





var theParams = {
action: 'search'
,
userId : userIdSValue

};
		
if (_page == true) {
	theParams.start = gridStart;
	theParams.limit = gridLimit;
}

return theParams;
}


function initGridSearch() {
userIdSField = new Ext.form.TextField({
    width: 100,
    labelWidth: 80,
    name: "userId"
    /*,id: "suserId"*/
});


initSearchFieldData();

} //end initGridSearch() function


function initGridMain() {

var theGridId = _compId;
if(theGridId == undefined || theGridId == null) {
	theGridId = "none";
}
		
//显示网格的组件
ucmFriendsCoverMainGrid = new Ext.grid.GridPanel({
    /*el: 'ucmFriendsCoverMainGrid',*/
    loadMask: theMask,
    ds: ucmFriendsCoverGridDataSource,
    sm: ucmFriendsCoverSmMain,
    cm: ucmFriendsCoverCmMain,
    autoWidth: true,
	height: 550,
    tbar: [
lang('langucmfriendscover.userid'), userIdSField, 

    	{
        text: lang('l_search'),
        handler: ucmFriendsCoverSearchForm
    },{
		text : '重置',
		handler : initSearchFieldData
	}],
    bbar: new Ext.PagingToolbar({
        pageSize: gridLimit,
        store: ucmFriendsCoverGridDataSource,
        displayInfo: true,
        displayMsg: lang('l_page_displaymsg'),
        emptyMsg: lang('l_page_emptymsg')
    }),
    buttonAlign: 'left',
    buttons: [{
        text: lang('l_add'),
        handler: function(){
            if (ucmFriendsCoverMainFormWin.isVisible()) {
                ucmFriendsCoverMainFormWin.hide();
            }
            else {
                ucmFriendsCoverMainFormWin.show();
                ucmFriendsCoverMainForm.getForm().reset();
            }
            
            ucmFriendsCoverFormChange('add',ucmFriendsCoverMainForm,ucmFriendsCoverMainFormWin);
        }
    }, {
        text: lang('l_view'),
        handler: function(){
            var selectedData = ucmFriendsCoverSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsCoverMainForm.form.load({
                    url: ucmFriendsCoverFormLoadUrl,
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
				
				ucmFriendsCoverFormChange('view',ucmFriendsCoverMainForm,ucmFriendsCoverMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsCoverAlertResult);
            }
        }
        
    }, {
        text: lang('l_edit'),
        handler: function(){
            var selectedData = ucmFriendsCoverSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsCoverMainForm.form.load({
                    url: ucmFriendsCoverFormLoadUrl,
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
				
				ucmFriendsCoverFormChange('edit',ucmFriendsCoverMainForm,ucmFriendsCoverMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsCoverAlertResult);
            }
        }
        
    }, {
        text: lang('l_delete'),
        handler: function(){
            var selectData = ucmFriendsCoverSmMain.getSelections();
            var selectDataStr = "";
            for (var i = 0; i < selectData.length; i++) {
                selectDataStr += selectData[i].data.uri + ",";
            }
            if (selectDataStr != '') {
                //debug('delete:' + selectDataStr);
                Ext.MessageBox.confirm(lang('l_confirm'), lang('l_confirm_delete'), function(confBtn){
                	if(confBtn == 'yes') {
                		Ext.Ajax.request({
                    		url: ucmFriendsCoverGridDeleteUrl,
                    		success: ucmFriendsCoverSearchForm,
                    		failure: ucmFriendsCoverSearchForm,
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
                Ext.MessageBox.alert('warn', 'warn delete msg', ucmFriendsCoverAlertResult);
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


var ucmFriendsCoverFormReader;
//value,display,desc
var ucmFriendsCoverFormFields;
var ucmFriendsCoverMainForm;
var ucmFriendsCoverMainFormWin;

function initGridForm() {

ucmFriendsCoverFormReader = new Ext.data.JsonReader({
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
        name: 'userId',
        mapping: 'userId',
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


ucmFriendsCoverFormFields = [
        new Ext.form.Hidden({
            name: 'uri'
            /*,id: 'uri'*/
        })
,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscover.userid'),
            allowBlank: false,
            name: 'userId'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscover.fileid'),
            allowBlank: false,
            name: 'fileId'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscover.extend1'),
            allowBlank: false,
            name: 'extend1'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscover.extend2'),
            allowBlank: false,
            name: 'extend2'
        }



        ]
        ;


//增加、修改表单
ucmFriendsCoverMainForm = new Ext.FormPanel({
    /*title: lang('l_ucmFriendsCover_form'),*/
    /*frame: true,*/
    border: false,
	bodyBorder: false,
    labelWidth: 110,
    bodyStyle: 'padding:0 10px 0;',
    reader: ucmFriendsCoverFormReader,
    items: ucmFriendsCoverFormFields,
    buttonAlign: 'left',
    buttons: [{
        //保存操作
        text: lang('l_save'),
        /*id: 'btnsave',*/
        handler: function(){
            
            
            
            ucmFriendsCoverMainForm.form.doAction('submit', { //将表单数据提交到服务器
                url: ucmFriendsCoverFormSaveUrl,
                method: 'post',
                success: function(form, action){
                    ucmFriendsCoverSearchForm();
                    Ext.MessageBox.alert(lang('l_info'), lang('l_message_success'), function(btn){
                    ucmFriendsCoverMainFormWin.hide();
                    
                    });
                },
                failure: function(form, action){
                	var responseRs = action.response.responseText;
                    Ext.MessageBox.alert(lang('l_error'), responseRs);
                    //ucmFriendsCoverMainFormWin.hide();
                }
            });
            
            
        }
    }, {
        //取消操作
        text: lang('l_cancel'),
        handler: function(){
            ucmFriendsCoverMainFormWin.hide();
            
        }
    }]
});


ucmFriendsCoverMainFormWin = new Ext.Window({
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
	items: ucmFriendsCoverMainForm
});

} //end initGridForm() function

function ucmFriendsCoverSearchForm(){
ucmFriendsCoverGridDataSource.load({
		params: getTheSearchParam(true)
	}
);
}

function ucmFriendsCoverFormChange(_action,_theForm, _theFormWin) {
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


function ucmFriendsCoverAlertResult(){

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
	return ucmFriendsCoverMainGrid;
}

this.searchData = function() {
	ucmFriendsCoverGridDataSource.load({
		params: {
			start: gridStart,
			limit: gridLimit
		}
	});
}

} //end object init
