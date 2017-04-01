var langucmfriendsshare = {
userid:'用户ID',
content:'分享内容',
createtime:'创建时间',
extend2:'备用字段2',
position:'地理位置',
extend1:'备用字段1'};

var UcmFriendsShareControl = function(_compId){
var gridStart = 0;
var gridLimit = 10;
var ucmFriendsShareGridDataSource;
var ucmFriendsShareGridSearchUrl = 'ucmFriendsShareData.jsp';
var ucmFriendsShareGridDeleteUrl = 'ucmFriendsShareData.jsp';
var ucmFriendsShareFormSaveUrl = 'ucmFriendsShareData.jsp?action=save';
var ucmFriendsShareFormLoadUrl = 'ucmFriendsShareData.jsp?action=getObject';


var theUploadFileUrl = 'uploadFile.do';
var theDownLoadUrl = 'downloadFile.do';
//GRID列
var ucmFriendsShareSmMain = new Ext.grid.CheckboxSelectionModel();

//列对应了下面的json数据,列中表示的index必须与下面的ucmFriendsShareGridDataSource对应上。
var ucmFriendsShareCmMain;

var userIdSField;


var ucmFriendsShareMainGrid;

//
function initGridColumn() {

ucmFriendsShareCmMain = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), ucmFriendsShareSmMain,
{
    header: lang('langucmfriendsshare.userid'),
    dataIndex: 'userId',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsshare.content'),
    dataIndex: 'content',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsshare.createtime'),
    dataIndex: 'createtime',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsshare.extend2'),
    dataIndex: 'extend2',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsshare.position'),
    dataIndex: 'position',
    sortable: false
}

 , 


{
    header: lang('langucmfriendsshare.extend1'),
    dataIndex: 'extend1',
    sortable: false
}




]);

//JSON数据,从后台获取的JSON数据映射到本地的Grid中
//proxy直接去读取josn数据
ucmFriendsShareGridDataSource = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: ucmFriendsShareGridSearchUrl
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
        name: 'content',
        mapping: 'content',
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
        name: 'extend2',
        mapping: 'extend2',
        type: 'string'
    }

,
    {
        name: 'position',
        mapping: 'position',
        type: 'string'
    }

,
    {
        name: 'extend1',
        mapping: 'extend1',
        type: 'string'
    }

    
    
    ])
});

ucmFriendsShareGridDataSource.on('beforeload', function(thisDs, options){
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
ucmFriendsShareMainGrid = new Ext.grid.GridPanel({
    /*el: 'ucmFriendsShareMainGrid',*/
    loadMask: theMask,
    ds: ucmFriendsShareGridDataSource,
    sm: ucmFriendsShareSmMain,
    cm: ucmFriendsShareCmMain,
    autoWidth: true,
	height: 550,
    tbar: [
lang('langucmfriendsshare.userid'), userIdSField, 

    	{
        text: lang('l_search'),
        handler: ucmFriendsShareSearchForm
    },{
		text : '重置',
		handler : initSearchFieldData
	}],
    bbar: new Ext.PagingToolbar({
        pageSize: gridLimit,
        store: ucmFriendsShareGridDataSource,
        displayInfo: true,
        displayMsg: lang('l_page_displaymsg'),
        emptyMsg: lang('l_page_emptymsg')
    }),
    buttonAlign: 'left',
    buttons: [{
        text: lang('l_add'),
        handler: function(){
            if (ucmFriendsShareMainFormWin.isVisible()) {
                ucmFriendsShareMainFormWin.hide();
            }
            else {
                ucmFriendsShareMainFormWin.show();
                ucmFriendsShareMainForm.getForm().reset();
            }
            
            ucmFriendsShareFormChange('add',ucmFriendsShareMainForm,ucmFriendsShareMainFormWin);
        }
    }, {
        text: lang('l_view'),
        handler: function(){
            var selectedData = ucmFriendsShareSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsShareMainForm.form.load({
                    url: ucmFriendsShareFormLoadUrl,
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
				
				ucmFriendsShareFormChange('view',ucmFriendsShareMainForm,ucmFriendsShareMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsShareAlertResult);
            }
        }
        
    }, {
        text: lang('l_edit'),
        handler: function(){
            var selectedData = ucmFriendsShareSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsShareMainForm.form.load({
                    url: ucmFriendsShareFormLoadUrl,
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
				
				ucmFriendsShareFormChange('edit',ucmFriendsShareMainForm,ucmFriendsShareMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsShareAlertResult);
            }
        }
        
    }, {
        text: lang('l_delete'),
        handler: function(){
            var selectData = ucmFriendsShareSmMain.getSelections();
            var selectDataStr = "";
            for (var i = 0; i < selectData.length; i++) {
                selectDataStr += selectData[i].data.uri + ",";
            }
            if (selectDataStr != '') {
                //debug('delete:' + selectDataStr);
                Ext.MessageBox.confirm(lang('l_confirm'), lang('l_confirm_delete'), function(confBtn){
                	if(confBtn == 'yes') {
                		Ext.Ajax.request({
                    		url: ucmFriendsShareGridDeleteUrl,
                    		success: ucmFriendsShareSearchForm,
                    		failure: ucmFriendsShareSearchForm,
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
                Ext.MessageBox.alert('warn', 'warn delete msg', ucmFriendsShareAlertResult);
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


var ucmFriendsShareFormReader;
//value,display,desc
var ucmFriendsShareFormFields;
var ucmFriendsShareMainForm;
var ucmFriendsShareMainFormWin;

function initGridForm() {

ucmFriendsShareFormReader = new Ext.data.JsonReader({
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
        name: 'content',
        mapping: 'content',
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
        name: 'extend2',
        mapping: 'extend2',
        type: 'string'
}

,
{
        name: 'position',
        mapping: 'position',
        type: 'string'
}

,
{
        name: 'extend1',
        mapping: 'extend1',
        type: 'string'
}


]);


ucmFriendsShareFormFields = [
        new Ext.form.Hidden({
            name: 'uri'
            /*,id: 'uri'*/
        })
,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsshare.userid'),
            allowBlank: false,
            name: 'userId'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsshare.content'),
            allowBlank: false,
            name: 'content'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsshare.createtime'),
            allowBlank: false,
            name: 'createtime'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsshare.extend2'),
            allowBlank: false,
            name: 'extend2'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsshare.position'),
            allowBlank: false,
            name: 'position'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendsshare.extend1'),
            allowBlank: false,
            name: 'extend1'
        }



        ]
        ;


//增加、修改表单
ucmFriendsShareMainForm = new Ext.FormPanel({
    /*title: lang('l_ucmFriendsShare_form'),*/
    /*frame: true,*/
    border: false,
	bodyBorder: false,
    labelWidth: 110,
    bodyStyle: 'padding:0 10px 0;',
    reader: ucmFriendsShareFormReader,
    items: ucmFriendsShareFormFields,
    buttonAlign: 'left',
    buttons: [{
        //保存操作
        text: lang('l_save'),
        /*id: 'btnsave',*/
        handler: function(){
            
            
            
            ucmFriendsShareMainForm.form.doAction('submit', { //将表单数据提交到服务器
                url: ucmFriendsShareFormSaveUrl,
                method: 'post',
                success: function(form, action){
                    ucmFriendsShareSearchForm();
                    Ext.MessageBox.alert(lang('l_info'), lang('l_message_success'), function(btn){
                    ucmFriendsShareMainFormWin.hide();
                    
                    });
                },
                failure: function(form, action){
                	var responseRs = action.response.responseText;
                    Ext.MessageBox.alert(lang('l_error'), responseRs);
                    //ucmFriendsShareMainFormWin.hide();
                }
            });
            
            
        }
    }, {
        //取消操作
        text: lang('l_cancel'),
        handler: function(){
            ucmFriendsShareMainFormWin.hide();
            
        }
    }]
});


ucmFriendsShareMainFormWin = new Ext.Window({
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
	items: ucmFriendsShareMainForm
});

} //end initGridForm() function

function ucmFriendsShareSearchForm(){
ucmFriendsShareGridDataSource.load({
		params: getTheSearchParam(true)
	}
);
}

function ucmFriendsShareFormChange(_action,_theForm, _theFormWin) {
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


function ucmFriendsShareAlertResult(){

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
	return ucmFriendsShareMainGrid;
}

this.searchData = function() {
	ucmFriendsShareGridDataSource.load({
		params: {
			start: gridStart,
			limit: gridLimit
		}
	});
}

} //end object init
