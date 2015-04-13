package pista.storage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;

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

import pista.Constants;
import pista.log.CustomLogging;
import pista.logic.Task;
import pista.parser.MainParser;

public class Storage {

	private static final Storage mStorage = new Storage();
	
	private static int max_number_of_tasks = 0;
	private String data_file_location = "";
	
	//Assertion
	private static final String ASSERT_ARRAY_LIST_TASK_NULL_MESSAGE = "List of task is not initialize";
	private static final String ASSERT_PARAMTER_ARRAY_LIST_TASK_NULL_MESSAGE = "Task list as a parameter is null";
	private static final String ASSERT_XML_FILE_PATH_EMPTY_MESSAGE = "XML file path is empty";
	private static final String ASSERT_XML_DOCUMENT_NULL_MESSAGE = "XML document is not initialize";
	private static final String ASSERT_XML_DOCUMENT_ROOT_NULL_MESSAGE = "XML root is undefined or null";
	private static final String ASSERT_XML_NODE_NULL_MESSAGE = "XML node is undefined or null";
	private static final String ASSERT_XML_NODE_LIST_NULL_MESSAGE = "XML node list is undefined or null";
	private static final String ASSERT_SOURCE_FILE_PATH_EMPTY_MESSAGE = "Source file path is empty";
	private static final String ASSERT_DESTINATION_FILE_PATH_EMPTY_MESSAGE = "Destination file path is empty";
	
	//Logging
	private static final String LOG_STORAGE_LOAD_SUCCESS = "Successfully Load XML file to task list";
	private static final String LOG_STORAGE_WRITE_XML_FILE_SUCCESS = "Successfully write into XML file";	
	private static final String LOG_STORAGE_SAVE_SUCCESS = "Successfully save XML file";
	private static final String LOG_STORAGE_SAVE_FAILURE = "Fail to save XML file";
	private static final String LOG_STORAGE_GET_NEXT_AVAILABLE_ID_SUCCESS = "Successfully get next available ID";
		
	private ArrayList<Task> taskList = null;
	private ArrayList<String> historyList = new ArrayList<String>();
	private static CustomLogging mLog = null;
	
	//@author A0125474E
	/**
	 * Don't allow to create a new instance
	 * **/
	private Storage(){} 
	
	//@author A0125474E
	/**This method will return the instance of Storage class
	 *and initialize Logging object 
	 ***/
	public static Storage getInstance(){ 
		if(mLog == null){
			mStorage.initLogging();
		}
		return mStorage;
	}
	
	//@author A0125474E
	/**Initialize Logging
	 * **/
	public boolean initLogging(){
		try{
			mLog = CustomLogging.getInstance(Storage.class.getName());
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
	}
	
	//@author A0125474E
	/**This method will load data from XML file
	 * **/
	public boolean load(){
		taskList = XmltoTable(getDataFileLocation());	
		if(taskList != null){
			return true;
		}
		return false;
	}
	
	//@author A0125474E
	/**This methodd will save from list to XML file
	 * **/
	public boolean save(){
		boolean isSaved = false;
		isSaved = tableToXml(getDataFileLocation(), taskList);
		return isSaved;
	}
	
	//@author A0125474E
	/**This method will write default string to the new XML file
	 * Parameters:	newPath - the new xml file path
	 * Return: 		boolean - true or false indicate success or fail
	 * **/
	public boolean writeNewXmlFile(String newPath){
		//filepath exist
		try {
			assert(newPath.isEmpty() == false) : "writeNewXmlFile: " + ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;

			File file = new File(newPath);

			if(!file.exists()){
				file.createNewFile();
			}
			
			//exist and either length is 0 or not valid xml format
			FileWriter fileWriter = new FileWriter(file, false); //overwrite the file
			
			BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
	        fileWriter.append(Constants.XML_DEFAULT_STRING); //write the default xml string
	        bufferFileWriter.close();
	        
			fileWriter.close();

			return true;
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		} catch (IOException e) {
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	//@author A0125474E
	/**This method will validate the given file by checking the important XML nodes
	 * Parameters:	xmlFilePath - the xml file path
	 * Return: 		boolean - true or false indicate valid or invalid
	 * **/
	public boolean isFileFormatValid(String xmlFilePath){
		//Do a small test on e selected xml file
		try{
			assert(xmlFilePath.isEmpty() == false) : "isFileFormatValid: " + ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;

			File mXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			Node nRoot = null;
			nRoot = doc.getDocumentElement(); //get root
			NodeList nTotalList = null;
			nTotalList = doc.getElementsByTagName(Constants.NODE_TOTAL_TASK_TAG); 
			Node nTotalValue = null;
			nTotalValue = nTotalList.item(0);
			NodeList nTaskList = null;
			nTaskList = doc.getElementsByTagName(Constants.NODE_TASK_TAG); 
			
			if(isNodeNull(nRoot) || isNodeNull(nTotalValue) || isNodeListNull(nTotalList) || isNodeListNull(nTaskList)){
				return false; //not valid XML format
			}
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		}catch (Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	//@author A0125474E
	/**This method will check if the given file path exist
	 * Parameters:	filePath - the xml file path
	 * Return: 		boolean - true or false indicate exist or non-exist
	 * **/
	public boolean isFileExist(String filePath){
		try{
			assert(filePath.isEmpty() == false) : "isFileExist: " + ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;
			
			
			File file = new File(filePath);
			if(file.exists()){
				return true;
			}
			//not exist
			return false;
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			 
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	//@author A0125474E
	/**This method will check if the given file contains any value
	 * Parameters:	filePath - the xml file path
	 * Return: 		boolean - true or false indicate empty or not empty
	 * **/
	public boolean isFileEmpty(String filePath){
		
		try {
			
			assert(filePath.isEmpty() == false) : "isFileEmpty: " + ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;
			
			File file = new File(filePath);
			FileInputStream fis = new FileInputStream(file);  

			int b = fis.read();
			if (b == -1)  {
				fis.close();
				return true; //is empty
			}
			
			fis.close();
			return false;
		
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return true;
				
		} catch (IOException e) {
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return true;
		}  
	
	} 
	
	//@author A0125474E
	/**This method will get the total current number of tasks
	 * Return:	int - number of tasks
	 * **/
	 public int getMaxNumberOfTasks(){
		 return max_number_of_tasks;
	 }
	 
	//@author A0125474E
	 /**This method will get the next usable task ID
		 * Return:	int - next non-recycled ID
		 * **/
	 public int getNextAvailableID(){
		 int lastID = 0;
		 
		 try{
			 assert(taskList != null) : ASSERT_ARRAY_LIST_TASK_NULL_MESSAGE;
			 
			 if(taskList != null){
				for(Task mTask : taskList){
					if(mTask.getID() > lastID){
						lastID = mTask.getID();
					}//end if 
				}//end for 
			 }//end if 
			  
			 mLog.logInfo(LOG_STORAGE_GET_NEXT_AVAILABLE_ID_SUCCESS);
			 return lastID + 1;
			 
		 }catch(AssertionError e){
			 mLog.logSevere(e.getMessage());
			 return -1;
			 
		 }catch(Exception e){
			 mLog.logSevere(e.getMessage());
			 return -1;
			 
		 }
		 
	 }
	 
	//@author A0125474E
	 /**This method will return current saved file location
	 * Return:	String - file location
	 * **/	 
	 public String getDataFileLocation(){
		 return data_file_location;
	 }
	 
	 //@author A0125474E
	 /**This method will initialize the task list
	 * Return:	boolean - true
	 * **/
	 public void initTaskList(){
		 this.taskList = new ArrayList<Task>();
	 }
	 
	 //@author A0112522Y
	 /**This method will get the task list
	 * Return:		ArrayList<Task>
	 * **/
	 public ArrayList<Task> getTaskList(){
		 if(this.taskList == null){
			 this.taskList = new ArrayList<Task>();
		 }
		 return this.taskList;
	 }
	 
	 //@author A0112522Y
	 /**This method will set the task list
		 * **/
	 public void setTaskList(ArrayList<Task> tl){
		 try{
			 assert(tl != null) : "setTaskList: " + ASSERT_PARAMTER_ARRAY_LIST_TASK_NULL_MESSAGE;
			 
			 if(tl != null){
				 this.taskList = new ArrayList<Task>(tl);
			 }	
		 }catch(AssertionError e){
			 mLog.logSevere(e.getMessage()); 
		 }
		 
	 }
	 
	 //@author A0112522Y
	 /**This method will get the history list
		 * Return:		ArrayList<String>
		 * **/
	 public ArrayList<String> getHistoryList(){
		 return this.historyList;
	 }
	 
	 //@author A0125474E
	 /**This method will copy entirely from source to destination file
	  * Parameters:		location - a new file location
	  * **/
	 public void setDataFileLocation(String location){
		 try{
			 assert(location.isEmpty() == false) : "setDataFileLocation: " + ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;
			 this.data_file_location = location;
			 
		 }catch(AssertionError e){
			 mLog.logSevere(e.getMessage());
		 } 
		 
	 }
	 
	 //@author A0125474E
	 /**This method will copy entirely from source to destination file
	  * Parameters:		sourceFilePath - valid source file path
	  * 				destinationFilePath - valid destination file path
	  * Return:			boolean - true or false - indicate success or fail
	  * **/
	 public boolean copyFile(String sourceFilePath, String destinationFilePath){
		 try{
			 
			 assert(sourceFilePath.isEmpty() == false) : "copyFile: " + ASSERT_SOURCE_FILE_PATH_EMPTY_MESSAGE;
			 assert(destinationFilePath.isEmpty() == false) : "copyFile: " + ASSERT_DESTINATION_FILE_PATH_EMPTY_MESSAGE;
			 
			 Path original = Paths.get(sourceFilePath); //original file 
			 Path destination = Paths.get(destinationFilePath); //new file 
			 Files.copy(original, destination, StandardCopyOption.REPLACE_EXISTING);
			 
			 return true;
		 }catch(AssertionError e){
			 mLog.logSevere(e.getMessage());
			 e.printStackTrace();
			 return false;
			 
		 }catch(IOException e){
			 mLog.logSevere(e.getMessage());
			 e.printStackTrace();
			 return false;
		 }
	 }
	 
	//@author A0125474E
	/**This method will check if the XML node is null
	 * Parameters:	n - a xml node
	 * Return: 		boolean - true or false indicate null or not null
	 * **/
	private boolean isNodeNull(Node n){
		if(n == null){
			return true;
		}
		return false;
	}
	
	//@author A0125474E
	/**This method will check if the XML node list collection is null
	 * Parameters:	n - a xml node list
	 * Return: 		boolean - true or false indicate null or not null
	 * **/
	private boolean isNodeListNull(NodeList n){
		if(n == null){
			return true;
		}
		return false;
	}
	
	//@author A0125474E
	/**This method will populate XML file from the task list
	 * Parameters:	xmlFilePath - given XML file path
	 * 				mArrayTask - given array list of tasks
	 * Return: 		boolean - true or false indicate success or fail
	 * **/
	private boolean tableToXml(String xmlFilePath, ArrayList<Task> mArrayTask){
		boolean isSaved = false;
		boolean isAllNodesRemove = false;
		
		try{	
			//assertion
			assert(mArrayTask != null) : ASSERT_ARRAY_LIST_TASK_NULL_MESSAGE;
			
			//start of XML
			Node nRoot = null;
			Node nTask = null;
			Node nTotal = null;

			File mXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			normalize(doc);
			
			//remove all nodes from XML
			isAllNodesRemove = xmlRemoveAllTask(doc);
			if(!isAllNodesRemove){ //if failed to remove all task nodes
				return false;
			}
			
			//create a new root node
			nRoot = doc.createElement(Constants.NODE_ROOT_TAG);
			
			//get the latest tasklist size as the total number of tasks
			max_number_of_tasks = mArrayTask.size();
			
			//add total number of tasks
			nTotal = doc.createElement(Constants.NODE_TOTAL_TASK_TAG);
			createNode(nTotal, Constants.NODE_TOTAL_TASK_VALUE_TAG, convertIntToString(max_number_of_tasks));//add value to total_task
			//add total node to root
			nRoot.appendChild(nTotal);
			
			//add task nodes
			for(Task mTask : mArrayTask){
				//new task node
				nTask = doc.createElement(Constants.NODE_TASK_TAG);

				//create new node method
				createNode(nTask, Constants.NODE_TASK_ID_TAG, convertIntToString(mTask.getID()));
				createNode(nTask, Constants.NODE_TASK_TITLE_TAG, mTask.getTitle());
				createNode(nTask, Constants.NODE_TASK_START_MILLISECOND_TAG, convertLongToString(mTask.getStartMilliseconds()));
				createNode(nTask, Constants.NODE_TASK_END_MILLISECOND_TAG, convertLongToString(mTask.getEndMilliseconds()));
				createNode(nTask, Constants.NODE_TASK_IS_DONE_TAG, convertBooleanToString(mTask.getIsDone()));
				createNode(nTask, Constants.NODE_TASK_CATEGORY_TAG, mTask.getCategory());
				createNode(nTask, Constants.NODE_TASK_PRIORITY_TAG, mTask.getPriority());
				createNode(nTask, Constants.NODE_TASK_REMINDER_TIME_TAG, convertLongToString(mTask.getReminder()));
				
				//add task node to root
				nRoot.appendChild(nTask);
				
			}//end for 
			
			//add root node to doc
			doc.appendChild(nRoot);
			
			//Save the document
			isSaved = saveXml(doc, xmlFilePath);			
	
			if (isSaved){
				mLog.logInfo(LOG_STORAGE_SAVE_SUCCESS);
				return true;
			}else{
				mLog.logWarning(LOG_STORAGE_SAVE_FAILURE);
				return false;
			}
			
		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			return false;
			
		}catch(Exception e){
			mLog.logSevere(e.getMessage()); //logging
			e.printStackTrace();
			return false;
		}
	
	}
	
	//@author A0125474E
	/**This method will populate task list from the XML file
	 * Parameters:	xmlFilePath - given XML file path
	 * Return: 		ArrayList<Task> - list of tasks
	 * **/
	private ArrayList<Task> XmltoTable(String xmlFilePath){
		
		ArrayList<Task> mArrayTask = new ArrayList<Task>();
		
		try{
			//assertion
			assert(!(xmlFilePath.equals("")) || !(xmlFilePath.isEmpty())) : ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;
			
			File mXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			normalize(doc);
			
			//Get total num of tasks from XML
			NodeList nTotalList = doc.getElementsByTagName(Constants.NODE_TOTAL_TASK_TAG);
			Node nTotalValue = nTotalList.item(0);
			max_number_of_tasks = convertStringToInt(nTotalValue.getTextContent()); //get total number
			
			NodeList nList = doc.getElementsByTagName(Constants.NODE_TASK_TAG);
			
			for (int temp = 0; temp < nList.getLength(); temp++) {
				 
				Node nNode = nList.item(temp);
		 
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {
		 
					Element eElement = (Element) nNode;
		 
					Task mTask = null;
					
					int m_id = 0;
					String m_title = "";
					String m_start_millisecond = "";
					String m_end_millisecond = "";
					String m_start_time = "";
					String m_end_time = "";
					String m_start_date = "";
					String m_end_date = "";
					String m_category = "";
					String m_priority = "";
					boolean m_is_done = false;
					String m_reminder = "";
					
					
					m_id = Integer.parseInt(eElement.getElementsByTagName(Constants.NODE_TASK_ID_TAG).item(0).getTextContent());
					m_title = eElement.getElementsByTagName(Constants.NODE_TASK_TITLE_TAG).item(0).getTextContent();
					m_start_millisecond = eElement.getElementsByTagName(Constants.NODE_TASK_START_MILLISECOND_TAG).item(0).getTextContent();
					m_end_millisecond = eElement.getElementsByTagName(Constants.NODE_TASK_END_MILLISECOND_TAG).item(0).getTextContent();
					
					m_start_time = MainParser.convertMillisecondToTime(Long.valueOf(m_start_millisecond));
					m_end_time = MainParser.convertMillisecondToTime(Long.valueOf(m_end_millisecond));
					m_start_date = MainParser.convertMillisecondToDate(Long.valueOf(m_start_millisecond));
					m_end_date = MainParser.convertMillisecondToDate(Long.valueOf(m_end_millisecond));
							
					m_is_done = Boolean.parseBoolean(eElement.getElementsByTagName(Constants.NODE_TASK_IS_DONE_TAG).item(0).getTextContent());
					m_category = eElement.getElementsByTagName(Constants.NODE_TASK_CATEGORY_TAG).item(0).getTextContent();
					m_priority = eElement.getElementsByTagName(Constants.NODE_TASK_PRIORITY_TAG).item(0).getTextContent();
					m_reminder = eElement.getElementsByTagName(Constants.NODE_TASK_REMINDER_TIME_TAG).item(0).getTextContent();
					
					//create new task object
					mTask = new Task(m_id, 
									m_title, 
									convertStringToLong(m_start_millisecond),
									convertStringToLong(m_end_millisecond),
									m_start_time,
									m_start_date,
									m_end_time,
									m_end_date,
									m_category,
									m_priority,
									m_is_done,
									convertStringToLong(m_reminder));
					
					mArrayTask.add(mTask); //add new task into table
					
				}//end if 
			}//end for
			
			mLog.logInfo(LOG_STORAGE_LOAD_SUCCESS);

		}catch(AssertionError e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			mArrayTask = null;
			
		}catch(Exception e){
			mLog.logSevere(e.getMessage());
			e.printStackTrace();
			mArrayTask = null;
		}//end try
		
		return mArrayTask;
		
	}//end XMLtoJava
	
	//@author A0125474E
	/**This method will add a child node to the parent node of XML
	 * Parameters:	parent - parent XML node
	 * 				tagName - child XML node name
	 * 				value - child XML node value
	 * **/
	private void createNode(Node parent, String tagName, String value){
		Document doc = parent.getOwnerDocument();
		//create a new node
		Node mNode  = doc.createElement(tagName);
		//set content of the node
		mNode.setTextContent(value);
		//add new node to the parent node
		parent.appendChild(mNode);
	}	

	//@author A0125474E
	/**This method will save XML document
	 * Parameters:	doc - XML document
	 * 				xmlFilePath - given XML file path
	 * Return: 		boolean - true or false indicate success or fail
	 * **/
	private boolean saveXml(Document doc, String xmlFilePath){
		//Save the document
		try{
			
			//assertion
			assert(doc != null): ASSERT_XML_DOCUMENT_NULL_MESSAGE;
			assert(!(xmlFilePath.equals("")) || !(xmlFilePath.isEmpty())) :	ASSERT_XML_FILE_PATH_EMPTY_MESSAGE;
			
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlFilePath));
			transformer.transform(source, result);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			
			mLog.logInfo(LOG_STORAGE_WRITE_XML_FILE_SUCCESS);
			
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
	
	//@author A0125474E
	/**This method will remove all child nodes from the given node
	 * Parameters:	node - given node that might contains child nodes
	 * Return: 		boolean - true or false indicate success or fail
	 * **/
	private boolean xmlRemoveAllTask(Node node) {
		try{
			assert(node != null) : ASSERT_XML_NODE_NULL_MESSAGE;
			NodeList mNodeList = node.getChildNodes();
			assert(mNodeList != null) : ASSERT_XML_NODE_LIST_NULL_MESSAGE;
			
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
	 	
	//@author A0125474E
	/**This method will restructure the string value in a XML node
	 * Parameters:	doc - XML document
	 * **/
	private void normalize(Document doc){
		 try{
			 assert(doc != null) : ASSERT_XML_DOCUMENT_NULL_MESSAGE;
			 
			 if(doc != null){
				 Element root = doc.getDocumentElement();
				 assert(root != null) : ASSERT_XML_DOCUMENT_ROOT_NULL_MESSAGE;
				 root.normalize();	
			 }
			 
		 }catch(AssertionError e){
			 mLog.logWarning(e.getMessage());
		 }	 	 
	 }

	 /*============================= Conversion =============================*/
	//@author A0125474E
	private String convertIntToString(int input){
		 return String.valueOf(input);
	}
	 
	 //@author A0125474E
	 private int convertStringToInt(String input){
		 return Integer.parseInt(input);
	 }
	 
	 //@author A0125474E
	 private String convertBooleanToString(boolean input){
		 return String.valueOf(input);
	 }
	 
	 //@author A0125474E
	 private String convertLongToString(long input){
		 return String.valueOf(input);
	 }
	 
	 //@author A0125474E
	 private long convertStringToLong(String input){
		 return Long.valueOf(input);
	 }

}//end class


