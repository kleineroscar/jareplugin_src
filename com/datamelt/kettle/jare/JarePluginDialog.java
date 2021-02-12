/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */ 
package com.datamelt.kettle.jare;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Group;
import org.pentaho.di.core.Const;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;

public class JarePluginDialog extends BaseStepDialog implements StepDialogInterface
{
	private JarePluginMeta input;

	private Label        wLabelRuleFile, wLabelProjectName, wLabelStepname, wLabelOutputType,wLabelStepMain, wLabelStepRuleResults, wLabelUseDB;
	private Text         wTextStepname;
	private Combo		 wComboOutputType, wComboStepRuleResults, wComboStepMain;
	private CCombo		 wConnection;
	private FormData     wFormBucket, wFormProjectName, wFormRuleFile, WFormSelectSource, wFormFileName,wFormStepname, wFormOutputType, wFormStepMain, wFormStepRuleResults;
	private TextVar      wTextRuleFile, wTextProjectName;
	private Button		 wbFilename, wbUseDB;
	private Group 		 wFileName;

	
	public JarePluginDialog(Shell parent, Object in, TransMeta transMeta, String sname)
	{
		super(parent, (BaseStepMeta)in, transMeta, sname);
		input=(JarePluginMeta)in;

	}

	public String open()
	{
		Shell parent = getParent();
		Display display = parent.getDisplay();
		
		shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
		props.setLook( shell );
        setShellImage(shell, input);
        
		ModifyListener lsMod = new ModifyListener() 
		{
			public void modifyText(ModifyEvent e) 
			{
				input.setChanged();
			}
		};
		changed = input.hasChanged();
		FormLayout formLayout = new FormLayout ();
		formLayout.marginWidth  = Const.FORM_MARGIN;
		formLayout.marginHeight = Const.FORM_MARGIN;

		shell.setLayout(formLayout);
		shell.setText(Messages.getString("JarePluginDialog.Shell.Title"));
		
		int middle = props.getMiddlePct();
		int margin = Const.MARGIN;

		// Stepname line
		wLabelStepname=new Label(shell, SWT.RIGHT);
		wLabelStepname.setText(Messages.getString("JarePluginDialog.StepName.Label"));
        props.setLook( wLabelStepname );
        wFormStepname=new FormData();
        wFormStepname.left = new FormAttachment(0, 0);
        wFormStepname.right= new FormAttachment(middle, -middle/2);
        wFormStepname.top  = new FormAttachment(0, margin);
		wLabelStepname.setLayoutData(wFormStepname);
		wTextStepname=new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
		wTextStepname.setText(stepname);
        props.setLook( wTextStepname );
        wTextStepname.addModifyListener(lsMod);
		wFormStepname=new FormData();
		wFormStepname.left = new FormAttachment(middle , 0);
		wFormStepname.top  = new FormAttachment(0, margin);
		wFormStepname.right= new FormAttachment(100, 0);
		wTextStepname.setLayoutData(wFormStepname);

		// Rule File Name line
		
		
		wLabelRuleFile=new Label(shell, SWT.RIGHT);
		wLabelRuleFile.setText(Messages.getString("JarePluginDialog.RuleFile.Label"));
        props.setLook( wLabelRuleFile );
        wFormRuleFile=new FormData();
        wFormRuleFile.left = new FormAttachment(0, 0);
        wFormRuleFile.right= new FormAttachment(middle, -margin);
        wFormRuleFile.top = new FormAttachment(wTextStepname, margin);
		wLabelRuleFile.setLayoutData(wFormRuleFile);
		
		wbFilename = new Button( shell, SWT.PUSH | SWT.CENTER );
	    wbFilename.setText(Messages.getString("JarePluginDialog.RuleFile.Browse"));
	    wbFilename.setToolTipText(Messages.getString("JarePluginDialog.RuleFile.Browse.Tooltip"));
	    props.setLook( wbFilename );
	    wFormFileName=new FormData();
	    //wFormFileName.left = new FormAttachment( wTextRuleFile, 0 );
	    wFormFileName.right= new FormAttachment(100, 0);
	    wFormFileName.top  = new FormAttachment(wTextStepname, margin);
	    wbFilename.setLayoutData(wFormFileName);
	    wbFilename.addSelectionListener( new SelectionAdapter() {
	      public void widgetSelected( SelectionEvent e ) {
	        FileDialog dialog = new FileDialog( shell, SWT.OPEN );
	        dialog.setFilterExtensions( new String[] { "*.zip", "*.xml", "*" } );
	        if ( dialog.open() != null ) {
	            wTextRuleFile.setText( dialog.getFilterPath() + System.getProperty( "file.separator" ) + dialog.getFileName() );
	            
	          }
	      }
	    } );
	    
		wTextRuleFile = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER );
		if(input.getRuleFileName()!=null)
		{
			wTextRuleFile.setText(input.getRuleFileName());
		}
		props.setLook( wTextRuleFile );
	    wTextRuleFile.addModifyListener( lsMod );
	    wFormBucket = new FormData();
	    wFormBucket.width=50;
	    wFormBucket.left = new FormAttachment( middle, 0 );
	    wFormBucket.top = new FormAttachment( wTextStepname, margin );
	    wFormBucket.right = new FormAttachment( 90, 0 );
	    wTextRuleFile.setLayoutData( wFormBucket );
		
		// DB select
		wLabelUseDB = new Label(shell, SWT.RIGHT);
		wLabelUseDB.setText(Messages.getString("JarePluginDialog.SelectSource.Label"));
		props.setLook( wLabelUseDB );
		wFormSelectSource = new FormData();
		wFormSelectSource.left = new FormAttachment( middle, 0);
		wFormSelectSource.top = new FormAttachment( wbFilename, margin );
		wFormSelectSource.right = new FormAttachment( middle, 10 + margin );
		wbUseDB.setLayoutData( wFormSelectSource );
		
		wbUseDB.addSelectionListener( new SelectionAdapter() {
			@Override
			public void widgetSelected( SelectionEvent e ) {
				Button source = (Button) e.getSource();

				if ( source.getSelection() ) {
					wbFilename.setEnabled(false);
					wTextRuleFile.setEnabled(false);
					wConnection.setEnabled(true);
					wTextProjectName.setEnabled(true);
				} else {
					wbFilename.setEnabled(true);
					wTextRuleFile.setEnabled(true);
					wConnection.setEnabled(false);
					wTextProjectName.setEnabled(false);
				}
			}
		});

		// Projectname field
		wLabelProjectName = new Label(shell, SWT.RIGHT);
		wLabelProjectName.setText(Messages.getString("JarePluginDialog.ProjectName.Label"));
		props.setLook( wLabelProjectName );
		wFormProjectName = new FormData();
		wFormProjectName.left = FormAttachment(wbUseDB, margin);
		wFormProjectName.right = FormAttachment(wbUseDB, 200 + margin);
		wFormProjectName.top = FormAttachment(wbFilename, margin);
		wLabelProjectName.setLayoutData(wFormProjectName);
		wTextProjectName = new TextVar( transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BODER );
		props.setLook( wTextProjectName );
		wTextProjectName.addModifyListener(lsMod);
		wFormProjectName = new FormData();
		wFormProjectName.left = FormAttachment(wLabelProjectName, margin);
		wFormProjectName.top = FormAttachment(wbFilename, margin);
		wFormProjectName.right = FormAttachment(100, -10 * margin );
		wTextProjectName.setLayoutData(wFormProjectName);

		//DB connection
		wConnection = addConnectionLine(shell, wTextProjectName, middle, margin);
		wConnection.addModifyListener( lsMod );
		
		// Main Output Step
		wLabelStepMain=new Label(shell, SWT.RIGHT);
		wLabelStepMain.setText(Messages.getString("JarePluginDialog.Step.Main"));
        props.setLook( wLabelStepMain );
        wFormStepMain=new FormData();
        wFormStepMain.left = new FormAttachment(0, 0);
        wFormStepMain.right= new FormAttachment(middle, -margin);
        wFormStepMain.top  = new FormAttachment(wConnection, margin);
        wLabelStepMain.setLayoutData(wFormStepMain);
		wComboStepMain=new Combo(shell, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		String outputSteps[] = transMeta.getNextStepNames(stepMeta);
		for(int i=0;i<outputSteps.length;i++)
		{
			wComboStepMain.add(outputSteps[i]);
		}
		if(input.getStepMain()!=null)
		{
			wComboStepMain.setText(input.getStepMain());
		}
		props.setLook( wComboStepMain );
		wComboStepMain.addModifyListener(lsMod);
		wFormStepMain=new FormData();
		wFormStepMain.left = new FormAttachment(middle, 0);
		wFormStepMain.top  = new FormAttachment(wConnection, margin);
		wFormStepMain.right= new FormAttachment(100, 0);
        wComboStepMain.setLayoutData(wFormStepMain);
		
		// Rule Results Output Step
		wLabelStepRuleResults=new Label(shell, SWT.RIGHT);
		wLabelStepRuleResults.setText(Messages.getString("JarePluginDialog.Step.RuleResults"));
        props.setLook( wLabelStepRuleResults );
        wFormStepRuleResults=new FormData();
        wFormStepRuleResults.left = new FormAttachment(0, 0);
        wFormStepRuleResults.right= new FormAttachment(middle, -margin);
        wFormStepRuleResults.top  = new FormAttachment(wComboStepMain, margin);
        wLabelStepRuleResults.setLayoutData(wFormStepRuleResults);
		wComboStepRuleResults=new Combo(shell, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		wComboStepRuleResults.add(Messages.getString("JarePluginDialog.Step.RuleResults.Type"));
		for(int i=0;i<outputSteps.length;i++)
		{
			wComboStepRuleResults.add(outputSteps[i]);
		}
		if(input.getStepRuleResults()!=null)
		{
			wComboStepRuleResults.setText(input.getStepRuleResults());
		}
		else
		{
			wComboStepRuleResults.select(0);
		}
		props.setLook( wComboStepRuleResults );
		wComboStepRuleResults.addModifyListener(lsMod);
        wFormStepRuleResults=new FormData();
        wFormStepRuleResults.left = new FormAttachment(middle, 0);
        wFormStepRuleResults.top  = new FormAttachment(wComboStepMain, margin);
        wFormStepRuleResults.right= new FormAttachment(100, 0);
        wComboStepRuleResults.setLayoutData(wFormStepRuleResults);
		
		// Rule Results Output Step Output Type
		wLabelOutputType=new Label(shell, SWT.RIGHT);
		wLabelOutputType.setText(Messages.getString("JarePluginDialog.OutputType.Label"));
        props.setLook( wLabelOutputType );
        wFormOutputType=new FormData();
        wFormOutputType.left = new FormAttachment(0, 0);
        wFormOutputType.right= new FormAttachment(middle, -margin);
        wFormOutputType.top  = new FormAttachment(wComboStepRuleResults, margin);
		wLabelOutputType.setLayoutData(wFormOutputType);
		wComboOutputType=new Combo(shell, SWT.VERTICAL | SWT.DROP_DOWN | SWT.BORDER | SWT.READ_ONLY);
		wComboOutputType.add(Messages.getString("JarePluginDialog.OutputType.ComboEntry_0"));
		wComboOutputType.add(Messages.getString("JarePluginDialog.OutputType.ComboEntry_1"));
		wComboOutputType.add(Messages.getString("JarePluginDialog.OutputType.ComboEntry_2"));
		wComboOutputType.add(Messages.getString("JarePluginDialog.OutputType.ComboEntry_3"));
		wComboOutputType.add(Messages.getString("JarePluginDialog.OutputType.ComboEntry_4"));
		wComboOutputType.select(input.getOutputType());
		props.setLook( wComboOutputType );
        wComboOutputType.addModifyListener(lsMod);
        wFormOutputType=new FormData();
        wFormOutputType.left = new FormAttachment(middle, 0);
        wFormOutputType.top  = new FormAttachment(wComboStepRuleResults, margin);
        wFormOutputType.right= new FormAttachment(100, 0);
		wComboOutputType.setLayoutData(wFormOutputType);
	
		// buttons
		wOK=new Button(shell, SWT.PUSH);
		wOK.setText(Messages.getString("System.Button.OK"));
		wCancel=new Button(shell, SWT.PUSH);
		wCancel.setText(Messages.getString("System.Button.Cancel")); //$NON-NLS-1$

        BaseStepDialog.positionBottomButtons(shell, new Button[] { wOK, wCancel}, margin, wComboOutputType);
        
		// Add listeners
		lsCancel   = new Listener() { public void handleEvent(Event e) { cancel(); } };
		lsOK       = new Listener() { public void handleEvent(Event e) { ok();     } };
		
		wCancel.addListener(SWT.Selection, lsCancel);
		wOK.addListener    (SWT.Selection, lsOK    );
		
		lsDef=new SelectionAdapter() { public void widgetDefaultSelected(SelectionEvent e) { ok(); } };
		
		wTextStepname.addSelectionListener( lsDef );
		wTextRuleFile.addSelectionListener( lsDef );
		wComboOutputType.addSelectionListener( lsDef );
		wComboStepMain.addSelectionListener( lsDef );
		wComboStepRuleResults.addSelectionListener( lsDef );
		
		// Detect X or ALT-F4 or something that kills this window...
		shell.addShellListener(	new ShellAdapter() { public void shellClosed(ShellEvent e) { cancel(); } } );

		// Set the shell size, based upon previous time...
		setSize();
		
		getData();
		input.setChanged(changed);
	
		shell.open();
		
		while (!shell.isDisposed())
		{
		    if (!display.readAndDispatch()) display.sleep();
		}
		return stepname;
	}
	
	// Read data from input
	public void getData()
	{
		if ( input.isDBUsed() ) {
			wConnection.setText( input.getDatabaseMeta().getName() );
			wTextProjectName.setText( input.getProjectName() );
		}
		wbUseDB.setSelection(input.isDBUsed());
		wConnection.setSelection(input.isDBUsed());
		wTextProjectName.setSelection(input.isDBUsed());

		wbFilename.setSelection(!input.isDBUsed());
		wTextRuleFile.setSelection(!input.isDBUsed());
		
		wTextStepname.selectAll();
		wTextStepname.setFocus();
	}
	
	private void cancel()
	{
		stepname=null;
		input.setChanged(changed);
		dispose();
	}
	
	private void ok()
	{
		stepname = wTextStepname.getText();
		input.setRuleFileName(wTextRuleFile.getText());
		input.setStepMain(wComboStepMain.getText());
		input.setStepRuleResults(wComboStepRuleResults.getText());
		input.setOutputType(wComboOutputType.getSelectionIndex());
		input.setDBUsed(wbUseDB.getSelection());
		if ( input.isDBUsed() ) {
			input.setDatabaseMeta( transMeta.findDatabase( wConnection.getText() ));
			input.setProjectName( wTextProjectName.getText() );
			if ( input.getDatabaseMeta() == null ) {
				input.setDBUsed( false );
				MessageBox mb = new MessageBox( shell, SWT.OK | SWT.ICON_ERROR );
				mb.setMessage( Messages.getString("JarePluginDialog.SelectValidConnection" ));
				mv.setText( Messages.getString("JarePluginDialog.DialogCaptionError" ));
				mb.open();
				return;
			}
		}
		
		input.setChanged(changed);
		dispose();
	}
}
