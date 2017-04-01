var langucmfriendscomment = {
shareuri:'分享ID',
fromuserid:'评论人',
touserid:'评论用户',
createtime:'评论时间',
content:'评论内容',
commenttype:'评论类型',
extend1:'扩展字段1',
extend2:'扩展字段2'};
var UcmFriendsCommentControl = function(_compId){
var gridStart = 0;
var gridLimit = 10;
var ucmFriendsCommentGridDataSource;
var ucmFriendsCommentGridSearchUrl = 'ucmFriendsCommentData.jsp';
var ucmFriendsCommentGridDeleteUrl = 'ucmFriendsCommentData.jsp';
var ucmFriendsCommentFormSaveUrl = 'ucmFriendsCommentData.jsp?action=save';
var ucmFriendsCommentFormLoadUrl = 'ucmFriendsCommentData.jsp?action=getObject';


var theUploadFileUrl = 'uploadFile.do';
var theDownLoadUrl = 'downloadFile.do';
//GRID列
var ucmFriendsCommentSmMain = new Ext.grid.CheckboxSelectionModel();

//列对应了下面的json数据,列中表示的index必须与下面的ucmFriendsCommentGridDataSource对应上。
var ucmFriendsCommentCmMain;

var shareUriSField;


var ucmFriendsCommentMainGrid;

//
function initGridColumn() {

ucmFriendsCommentCmMain = new Ext.grid.ColumnModel([new Ext.grid.RowNumberer(), ucmFriendsCommentSmMain,
{
    header: lang('langucmfriendscomment.shareuri'),
    dataIndex: 'shareUri',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscomment.fromuserid'),
    dataIndex: 'fromUserId',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscomment.touserid'),
    dataIndex: 'toUserId',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscomment.createtime'),
    dataIndex: 'createtime',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscomment.content'),
    dataIndex: 'content',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscomment.commenttype'),
    dataIndex: 'commentType',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscomment.extend1'),
    dataIndex: 'extend1',
    sortable: false
}

 , 


{
    header: lang('langucmfriendscomment.extend2'),
    dataIndex: 'extend2',
    sortable: false
}




]);

//JSON数据,从后台获取的JSON数据映射到本地的Grid中
//proxy直接去读取josn数据
ucmFriendsCommentGridDataSource = new Ext.data.Store({
    proxy: new Ext.data.HttpProxy({
        url: ucmFriendsCommentGridSearchUrl
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
        name: 'fromUserId',
        mapping: 'fromUserId',
        type: 'string'
    }

,
    {
        name: 'toUserId',
        mapping: 'toUserId',
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
        name: 'content',
        mapping: 'content',
        type: 'string'
    }

,
    {
        name: 'commentType',
        mapping: 'commentType',
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

ucmFriendsCommentGridDataSource.on('beforeload', function(thisDs, options){
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
ucmFriendsCommentMainGrid = new Ext.grid.GridPanel({
    /*el: 'ucmFriendsCommentMainGrid',*/
    loadMask: theMask,
    ds: ucmFriendsCommentGridDataSource,
    sm: ucmFriendsCommentSmMain,
    cm: ucmFriendsCommentCmMain,
    autoWidth: true,
	height: 550,
    tbar: [
lang('langucmfriendscomment.shareuri'), shareUriSField, 

    	{
        text: lang('l_search'),
        handler: ucmFriendsCommentSearchForm
    },{
		text : '重置',
		handler : initSearchFieldData
	}],
    bbar: new Ext.PagingToolbar({
        pageSize: gridLimit,
        store: ucmFriendsCommentGridDataSource,
        displayInfo: true,
        displayMsg: lang('l_page_displaymsg'),
        emptyMsg: lang('l_page_emptymsg')
    }),
    buttonAlign: 'left',
    buttons: [{
        text: lang('l_add'),
        handler: function(){
            if (ucmFriendsCommentMainFormWin.isVisible()) {
                ucmFriendsCommentMainFormWin.hide();
            }
            else {
                ucmFriendsCommentMainFormWin.show();
                ucmFriendsCommentMainForm.getForm().reset();
            }
            
            ucmFriendsCommentFormChange('add',ucmFriendsCommentMainForm,ucmFriendsCommentMainFormWin);
        }
    }, {
        text: lang('l_view'),
        handler: function(){
            var selectedData = ucmFriendsCommentSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsCommentMainForm.form.load({
                    url: ucmFriendsCommentFormLoadUrl,
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
				
				ucmFriendsCommentFormChange('view',ucmFriendsCommentMainForm,ucmFriendsCommentMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsCommentAlertResult);
            }
        }
        
    }, {
        text: lang('l_edit'),
        handler: function(){
            var selectedData = ucmFriendsCommentSmMain.getSelected();
            if (selectedData != null) {
                ucmFriendsCommentMainForm.form.load({
                    url: ucmFriendsCommentFormLoadUrl,
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
				
				ucmFriendsCommentFormChange('edit',ucmFriendsCommentMainForm,ucmFriendsCommentMainFormWin);
            }
            else {
                Ext.MessageBox.alert(lang('l_warn'), lang('l_warn_editmsg'), ucmFriendsCommentAlertResult);
            }
        }
        
    }, {
        text: lang('l_delete'),
        handler: function(){
            var selectData = ucmFriendsCommentSmMain.getSelections();
            var selectDataStr = "";
            for (var i = 0; i < selectData.length; i++) {
                selectDataStr += selectData[i].data.uri + ",";
            }
            if (selectDataStr != '') {
                //debug('delete:' + selectDataStr);
                Ext.MessageBox.confirm(lang('l_confirm'), lang('l_confirm_delete'), function(confBtn){
                	if(confBtn == 'yes') {
                		Ext.Ajax.request({
                    		url: ucmFriendsCommentGridDeleteUrl,
                    		success: ucmFriendsCommentSearchForm,
                    		failure: ucmFriendsCommentSearchForm,
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
                Ext.MessageBox.alert('warn', 'warn delete msg', ucmFriendsCommentAlertResult);
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


var ucmFriendsCommentFormReader;
//value,display,desc
var ucmFriendsCommentFormFields;
var ucmFriendsCommentMainForm;
var ucmFriendsCommentMainFormWin;

function initGridForm() {

ucmFriendsCommentFormReader = new Ext.data.JsonReader({
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
        name: 'fromUserId',
        mapping: 'fromUserId',
        type: 'string'
}

,
{
        name: 'toUserId',
        mapping: 'toUserId',
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
        name: 'content',
        mapping: 'content',
        type: 'string'
}

,
{
        name: 'commentType',
        mapping: 'commentType',
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


ucmFriendsCommentFormFields = [
        new Ext.form.Hidden({
            name: 'uri'
            /*,id: 'uri'*/
        })
,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.shareuri'),
            allowBlank: false,
            name: 'shareUri'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.fromuserid'),
            allowBlank: false,
            name: 'fromUserId'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.touserid'),
            allowBlank: false,
            name: 'toUserId'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.createtime'),
            allowBlank: false,
            name: 'createtime'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.content'),
            allowBlank: false,
            name: 'content'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.commenttype'),
            allowBlank: false,
            name: 'commentType'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.extend1'),
            allowBlank: false,
            name: 'extend1'
        }

,
        {
        	xtype: 'textfield',
            fieldLabel: lang('langucmfriendscomment.extend2'),
            allowBlank: false,
            name: 'extend2'
        }



        ]
        ;


//增加、修改表单
ucmFriendsCommentMainForm = new Ext.FormPanel({
    /*title: lang('l_ucmFriendsComment_form'),*/
    /*frame: true,*/
    border: false,
	bodyBorder: false,
    labelWidth: 110,
    bodyStyle: 'padding:0 10px 0;',
    reader: ucmFriendsCommentFormReader,
    items: ucmFriendsCommentFormFields,
    buttonAlign: 'left',
    buttons: [{
        //保存操作
        text: lang('l_save'),
        /*id: 'btnsave',*/
        handler: function(){
            
            
            
            ucmFriendsCommentMainForm.form.doAction('submit', { //将表单数据提交到服务器
                url: ucmFriendsCommentFormSaveUrl,
                method: 'post',
                success: function(form, action){
                    ucmFriendsCommentSearchForm();
                    Ext.MessageBox.alert(lang('l_info'), lang('l_message_success'), function(btn){
                    ucmFriendsCommentMainFormWin.hide();
                    
                    });
                },
                failure: function(form, action){
                	var responseRs = action.response.responseText;
                    Ext.MessageBox.alert(lang('l_error'), responseRs);
                    //ucmFriendsCommentMainFormWin.hide();
                }
            });
            
            
        }
    }, {
        //取消操作
        text: lang('l_cancel'),
        handler: function(){
            ucmFriendsCommentMainFormWin.hide();
            
        }
    }]
});


ucmFriendsCommentMainFormWin = new Ext.Window({
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
	items: ucmFriendsCommentMainForm
});

} //end initGridForm() function

function ucmFriendsCommentSearchForm(){
ucmFriendsCommentGridDataSource.load({
		params: getTheSearchParam(true)
	}
);
}

function ucmFriendsCommentFormChange(_action,_theForm, _theFormWin) {
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


function ucmFriendsCommentAlertResult(){

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
	return ucmFriendsCommentMainGrid;
}

this.searchData = function() {
	ucmFriendsCommentGridDataSource.load({
		params: {
			start: gridStart,
			limit: gridLimit
		}
	});
}

} //end object init
