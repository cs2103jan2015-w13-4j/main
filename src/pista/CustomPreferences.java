package pista;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.prefs.Preferences;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import pista.log.CustomLogging;
import pista.storage.Storage;


public class CustomPreferences {

	private static CustomPreferences mCustomPref = new CustomPreferences();
	private static CustomLogging mLog = null;
	
	private Preferences mPrefs = null;
	private String _fileLocation = "";
	
	private CustomPreferences(){} //make sure this class cannot be instantiate
	
	public static CustomPreferences getInstance(){
		return mCustomPref;
	}
	
	public boolean initLogging(){
		try{
			mLog = CustomLogging.getInstance(Storage.class.getName());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}
	
	public boolean load(){
		boolean isExist = false;
		boolean isEmpty = false;
		boolean isValid = false;
		boolean isCreate = false;
		boolean isLoad = false;
		
		isExist = isFileExist(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);
		if(!isExist){
			isCreate = createNewPreference(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);
			return isCreate;
		}
		
		isEmpty = isFileEmpty(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);
		if(isEmpty){
			isCreate = createNewPreference(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);
			return isCreate;
		}
		
		isValid = isFileFormatValid(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);
		if(!isValid){
			return false;
		}
		
		isLoad = loadPreference(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);

		return isLoad;
	}
	
	public boolean save(){
		boolean isSave = false;
		isSave = savePreference(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);
		return isSave;
	}
	
	public boolean createNewPreference(String newPath){
		//filepath exist
		try {
			String newXmlString = Constants.PREFERENCE_XML_DEFAULT_STRING;
			File file = new File(Constants.PREFERENCE_XML_DEFAULT_FILE_PATH);

			//exist and either length is 0 or not valid xml format
			FileWriter fileWriter = new FileWriter(file, false); //overwrite the file
			
			BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
	        fileWriter.append(newXmlString); //write the default xml string
	        bufferFileWriter.close();
	        
			fileWriter.close();

			return true;
		} catch (IOException e) {
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	private boolean loadPreference(String fileName){
		try{
			File mXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			normalize(doc);
			
			//Get file location from XML
			NodeList nTotalList = doc.getElementsByTagName(Constants.PREFERENCE_XML_FILE_LOCATION_NODE);
			Node nTotalValue = nTotalList.item(0);
			_fileLocation = nTotalValue.getTextContent(); //get preferences file location
			
			return true;
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		}//end try

	}
	
	private boolean savePreference(String fileName){
		boolean isSaved = false;
		boolean isAllNodesRemove = false;
		
		try{
			Node nRoot = null;
			Node nSetting = null;
			
			File mXmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			normalize(doc);
			
			isAllNodesRemove = xmlRemoveAllTask(doc);
			if(!isAllNodesRemove){ //if failed to remove all task nodes
				return false;
			}

			//create root node
			nRoot = doc.createElement(Constants.PREFERENCE_XML_ROOT_NODE);
			
			//create setting node
			nSetting = doc.createElement(Constants.PREFERENCE_XML_SETTING_NODE);	
			createNode(nSetting, Constants.PREFERENCE_XML_FILE_LOCATION_NODE, _fileLocation);//add value to total_task

			//add setting node to root
			nRoot.appendChild(nSetting);
			
			//add root node to doc
			doc.appendChild(nRoot);
			
			//Save the document
			isSaved = saveXml(doc, fileName);		
			
			if (isSaved){
				//mLog.logInfo(LOG_STORAGE_SAVE_SUCCESS);
				return true;
			}else{
				//mLog.logWarning(LOG_STORAGE_SAVE_FAILURE);
				return false;
			}
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;	
			
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		}//end try
		
	}
	

	
	private void createNode(Node parent, String tagName, String value){
		Document doc = parent.getOwnerDocument();
		//create a new node
		Node mNode  = doc.createElement(tagName);
		//set content of the node
		mNode.setTextContent(value);
		//add new node to the parent node
		parent.appendChild(mNode);
	}	
	
	private boolean saveXml(Document doc, String xmlFilePath){
		//Save the document
		try{
			//assertion
			//assert(doc != null): ASSERT_XML_DOCUMENT_NULL_MESSAGE;
			//assert(!(xmlFilePath.equals("")) || !(xmlFilePath.isEmpty())) :	ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlFilePath));
			transformer.transform(source, result);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			
			//mLog.logInfo(LOG_STORAGE_WRITE_XML_FILE_SUCCESS);
			
			return true;
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;		
		}//end try
		
	}//end saveXML
	
	private void normalize(Document doc){
		 //normalize is not a complusory to do
		 try{
			// assert(doc != null) : ASSERT_XML_DOCUMENT_NULL_MESSAGE;
			 
			 if(doc != null){
				 Element root = doc.getDocumentElement();
				// assert(root != null) : ASSERT_XML_DOCUMENT_ROOT_NULL_MESSAGE;
				 root.normalize();	
			 }
			 
		 }catch(AssertionError e){
			 mLog.logWarning(e.getMessage());
		 }
		 	 
	 }
	
	private boolean xmlRemoveAllTask(Node node) {
		try{
			
			//assert(node != null) : ASSERT_XML_NODE_NULL_MESSAGE;
			NodeList mNodeList = node.getChildNodes();
			//assert(mNodeList != null) : ASSERT_XML_NODE_LIST_NULL_MESSAGE;
			
			if(mNodeList != null){
				for(int i=0; i< mNodeList.getLength(); i++){
					
					Node n = mNodeList.item(i);  
					node.removeChild(n);
				}//end for	
			}
			
			return true;
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			return false;
			
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			return false;
		}
		
		
	}//end removeAllFromXML
	
	private boolean isNodeNull(Node n){
		if(n == null){
			return true;
		}
		return false;
	}
	
	private boolean isNodeListNull(NodeList n){
		if(n == null){
			return true;
		}
		return false;
	}
	
	public boolean isFileExist(String filePath){

		try{
			File file = new File(filePath);
			if(file.exists()){
				return true;
			}
			//not exist
			return false;
			
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
		}

	}
	
	public boolean isFileEmpty(String filePath){
		
		try {
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);  

			int b = fis.read();
			if (b == -1)  {
				return true; //is empty
			}
			
			return false;
		
		} catch (IOException e) {
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return true;
		}  
	
	} 
	
	public boolean isFileFormatValid(String xmlFilePath){
		//Do a small test on e selected xml file
		try{
			File mXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			Node nRoot = doc.getDocumentElement(); //get root
			NodeList nSettingList = doc.getElementsByTagName(Constants.PREFERENCE_XML_SETTING_NODE);
			Node nSetting = nSettingList.item(0);
			NodeList nFileLocList = doc.getElementsByTagName(Constants.PREFERENCE_XML_FILE_LOCATION_NODE); 
			Node nFileLocation = nFileLocList.item(0);

			if(isNodeNull(nRoot) || isNodeNull(nSetting) || isNodeNull(nFileLocation) ||
					isNodeListNull(nSettingList) || isNodeListNull(nFileLocList)){
				return false; //not valid XML format
			}

		}catch (Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	public String getFileLocation(){
		return this._fileLocation;
	}
	
	public void setFileLocation(String loc){
		this._fileLocation = loc;
	}
	
	
/*
	public boolean initPreference(String className){
		try{
			//mPrefs = Preferences.userNodeForPackage(getClass());
			mPrefs = Preferences.userRoot().node(className);
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			
			return false;
		}
		
	}
	
	public boolean savePreference(String name, String value){
		try{
			if(mPrefs == null){			
				initPreference(getClass().getName());
			}	
			mPrefs.put(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean savePreference(String name, int value){
		try{
			if(mPrefs == null){
				initPreference(getClass().getName());
			}
			mPrefs.putInt(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}

	public boolean savePreference(String name, boolean value){
		try{
			if(mPrefs == null){
				initPreference(getClass().getName());
			}
			mPrefs.putBoolean(name, value);
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	public String getPreferenceStringValue(String name){
		if(mPrefs == null){ return ""; }
		String value = mPrefs.get(name, "");
		return value;
	}
	
	public int getPreferenceIntValue(String name){
		if(mPrefs == null){ return 0; }
		int value = mPrefs.getInt(name, 0);
		return value;
	}
	
	public boolean getPreferenceBooleanValue(String name){
		if(mPrefs == null){ return false; }
		boolean value = mPrefs.getBoolean(name, false);
		return value;
	}
	
	*/
	
}
