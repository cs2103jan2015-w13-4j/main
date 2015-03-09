package logic.storage;

import logic.Parser;
import logic.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;


public class Storage {

	static int max_number_of_tasks;
	
	static final String NODE_ROOT_TAG = "tasks";
	static final String NODE_TOTAL_TASK_TAG = "total_task";
	static final String NODE_TOTAL_TASK_VALUE_TAG = "value";
	static final String NODE_TASK_TAG = "task";
	static final String NODE_TASK_ID_TAG = "id";
	static final String NODE_TASK_TITLE_TAG = "title";
	static final String NODE_TASK_START_MILLISECOND_TAG = "start_millisecond";
	static final String NODE_TASK_END_MILLISECOND_TAG = "end_millisecond";
	static final String NODE_TASK_IS_DONE_TAG = "is_done";
	/*
	final String NODE_TASK_START_TIME_TAG = "start_time";
	final String NODE_TASK_START_DATE_TAG = "start_date";
	final String NODE_TASK_END_TIME_TAG = "end_time";
	final String NODE_TASK_END_DATE_TAG = "end_date";
	*/
	
	
	
	//default constructor
	public Storage(){
	}
	
	/*
	 * Need XML file path to read
	 * ArrayList of tasks for input
	 * */
	public static boolean tableToXml(String xmlFilePath, ArrayList<Task> mArrayTask){
		boolean isSaved = false;
		try{
			Node nRoot = null;
			Node nTask = null;
			Node nTotal = null;
			
			File mXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			normalize(doc);
			
			//remove all nodes from XML
			xmlRemoveAllTask(doc);
			
			//create a new root node
			nRoot = doc.createElement(NODE_ROOT_TAG);
			
			//add total number of tasks
			nTotal = doc.createElement(NODE_TOTAL_TASK_TAG);
			createNode(nTotal, NODE_TOTAL_TASK_VALUE_TAG, convertIntToString(max_number_of_tasks));
			//add total node to root
			nRoot.appendChild(nTotal);
			
			
			//add task nodes
			for(Task mTask : mArrayTask){
				//new task node
				nTask = doc.createElement(NODE_TASK_TAG);

				//create new node method
				createNode(nTask, NODE_TASK_ID_TAG, convertIntToString(mTask.getID()));
				createNode(nTask, NODE_TASK_TITLE_TAG, mTask.getTitle());
				createNode(nTask, NODE_TASK_START_MILLISECOND_TAG, convertLongToString(mTask.getStartMilliseconds()));
				createNode(nTask, NODE_TASK_END_MILLISECOND_TAG, convertLongToString(mTask.getEndMilliseconds()));
				/*
				createNode(nTask, NODE_TASK_START_TIME_TAG, mTask.getStartTime());
				createNode(nTask, NODE_TASK_END_TIME_TAG, mTask.getEndTime());
				createNode(nTask, NODE_TASK_START_DATE_TAG, mTask.getStartDate());
				createNode(nTask, NODE_TASK_END_DATE_TAG, mTask.getEndDate());
				*/
				createNode(nTask, NODE_TASK_IS_DONE_TAG, convertBooleanToString(mTask.getIsDone()));
				
				//add task node to root
				nRoot.appendChild(nTask);
				
			}//end for 
			
			//add root node to doc
			doc.appendChild(nRoot);
			
			//Save the document
			isSaved = saveXml(doc, xmlFilePath);			
	
			if (isSaved){
				return true;
			}else{
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
	
		
	}
	
	
	
	/*
	 * ArrayList
	 * value  task object
	 * */
	public static ArrayList<Task> XmltoTable(String xmlFilePath){
		
		ArrayList<Task> mArrayTask = new ArrayList<Task>();
		
		try{
			File mXmlFile = new File(xmlFilePath);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(mXmlFile);
			
			normalize(doc);
			
			NodeList nTotalList = doc.getElementsByTagName(NODE_TOTAL_TASK_TAG);
			Node nTotalValue = nTotalList.item(0);
			max_number_of_tasks = convertStringToInt(nTotalValue.getTextContent());
			
			NodeList nList = doc.getElementsByTagName(NODE_TASK_TAG);
			
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
					boolean m_is_done = false;
					
					
					m_id = Integer.parseInt(eElement.getElementsByTagName(NODE_TASK_ID_TAG).item(0).getTextContent());
					m_title = eElement.getElementsByTagName(NODE_TASK_TITLE_TAG).item(0).getTextContent();
					m_start_millisecond = eElement.getElementsByTagName(NODE_TASK_START_MILLISECOND_TAG).item(0).getTextContent();
					m_end_millisecond = eElement.getElementsByTagName(NODE_TASK_END_MILLISECOND_TAG).item(0).getTextContent();
					
					m_start_time = Parser.convertMillisecondToTime(Long.valueOf(m_start_millisecond));
					m_end_time = Parser.convertMillisecondToTime(Long.valueOf(m_end_millisecond));
					m_start_date = Parser.convertMillisecondToDate(Long.valueOf(m_start_millisecond));
					m_end_date = Parser.convertMillisecondToDate(Long.valueOf(m_end_millisecond));
							
					m_is_done = Boolean.parseBoolean(eElement.getElementsByTagName(NODE_TASK_IS_DONE_TAG).item(0).getTextContent());
					
					
					//create new task object
					mTask = new Task(m_id, 
									m_title, 
									convertStringToLong(m_start_millisecond),
									convertStringToLong(m_end_millisecond),
									m_start_time,
									m_start_date,
									m_end_time,
									m_end_date,
									m_is_done);
					
					mArrayTask.add(mTask); //add new task into table
					
				}//end if 
			}//end for
			
			
			
		}catch(Exception e){
			e.printStackTrace();
		}//end try
		
		
		
		
		return mArrayTask;
		
	}//end XMLtoJava
	
	

	public static boolean XmlAddTask(String xmlFilePath, Task newTask){
		boolean isSaved = false;
		
		//increase the total tasks by 1
		max_number_of_tasks = max_number_of_tasks + 1;
		
		try{

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlFilePath);
		
			normalize(doc);
			
			//get root node
			Node nRoot = doc.getFirstChild();
			
			Node ntotalTask = doc.getElementsByTagName(NODE_TOTAL_TASK_TAG).item(0);
			Node nTotalTaskValue = ntotalTask.getFirstChild();
			nTotalTaskValue.setTextContent(String.valueOf(max_number_of_tasks));
			
			//Create a new child of the root
			Element nTask = doc.createElement(NODE_TASK_TAG);
			
			//create new node method
			createNode(nTask, NODE_TASK_ID_TAG, convertIntToString(newTask.getID()));
			createNode(nTask, NODE_TASK_TITLE_TAG, newTask.getTitle());
			createNode(nTask, NODE_TASK_START_MILLISECOND_TAG, convertLongToString(newTask.getStartMilliseconds()));
			createNode(nTask, NODE_TASK_END_MILLISECOND_TAG, convertLongToString(newTask.getEndMilliseconds()));
			createNode(nTask, NODE_TASK_IS_DONE_TAG, convertBooleanToString(newTask.getIsDone()));
			/*
			createNode(nTask, NODE_TASK_START_TIME_TAG, newTask.getStartTime());
			createNode(nTask, NODE_TASK_END_TIME_TAG, newTask.getEndTime());
			createNode(nTask, NODE_TASK_START_DATE_TAG, newTask.getStartDate());
			createNode(nTask, NODE_TASK_END_DATE_TAG, newTask.getEndDate());
			
			*/
			
			//add new task node to root
			nRoot.appendChild(nTask);
		
			//Save the document
			isSaved = saveXml(doc, xmlFilePath);
					
			
			if (isSaved){
				return true;
			}else{
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}	
		
	}
	
	//Not in used
	private static boolean xmlDeleteTask(String xmlFilePath, Task deleteTask){
		boolean isSaved = false;
		
		try{

			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(xmlFilePath);
		
			//normalize(doc);
			Node nRoot = doc.getFirstChild();
			
			XPathFactory xpf = XPathFactory.newInstance();
	        XPath xpath = xpf.newXPath();
	        
	        //find task node with specific ID (//tasks/task[id=??])
	        XPathExpression expression = xpath.compile("//"+ NODE_ROOT_TAG +"/" + NODE_TASK_TAG +"["+ NODE_TASK_ID_TAG +"="+ deleteTask.getID() +"]");

	        //get task node
	        Node removeNode = (Node) expression.evaluate(doc, XPathConstants.NODE);
	        
	        //remove task node from root
	        nRoot.removeChild(removeNode);
		
			//Save the document
			isSaved = saveXml(doc, xmlFilePath);
					
			
			if (isSaved){
				return true;
			}else{
				return false;
			}
			
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	
	private static void createNode(Node parent, String tagName, String value){
		Document doc = parent.getOwnerDocument();
		
		//create a new node
		Node mNode  = doc.createElement(tagName);
		//set content of the node
		mNode.setTextContent(value);
		
		//add new node to the parent node
		parent.appendChild(mNode);
		
	}	
	
	private static Node searchNode(Document doc, String findID){
		
		Node findNode = null;
		Node nRoot = doc.getDocumentElement();
		NodeList nl = nRoot.getChildNodes();
		
		for(int i=0; i< nl.getLength(); i++){
			
			Node nID = nl.item(i).getChildNodes().item(0);
			
			if(nID.getTextContent().equals(findID)){
				return nID;
			}
		}
		
		return null;		
		
	}
	
	
	private static boolean saveXml(Document doc, String xmlFilePath){
		//Save the document
		try{
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(xmlFilePath));
			transformer.transform(source, result);
			transformer.setOutputProperty(OutputKeys.INDENT, "yes"); 
			
			return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;		
		}//end try
		
	}//end saveXML
	
	
	
	private static void xmlRemoveAllTask(Node node) {
		 
		 NodeList nl = node.getChildNodes();
		 for(int i=0; i< nl.getLength(); i++){
			
			 Node n = nl.item(i); 
				 
			 node.removeChild(n);
		 }//end for	 
		 
	}//end removeAllFromXML
	 
	 
	 private static void normalize(Document doc){
		 Element root = doc.getDocumentElement();
		 root.normalize();		 
	 }

	 public static int getMaxNumberOfTasks(){
		 return max_number_of_tasks;
	 }
	 
	 private static void setMaxNumberOfTasks(int num){
		 max_number_of_tasks = num;
	 }
	 
	 public static int getNextAvailableID(){
		 return max_number_of_tasks + 1;
	 }
	 
	 
	 
	 /*Conversion*/
	 private static String convertIntToString(int input){
		 return String.valueOf(input);
	 }
	 
	 private static int convertStringToInt(String input){
		 return Integer.parseInt(input);
	 }
	 
	 private static String convertBooleanToString(boolean input){
		 return String.valueOf(input);
	 }
	 
	 private static String convertLongToString(long input){
		 return String.valueOf(input);
	 }
	 
	 private static long convertStringToLong(String input){
		 return Long.valueOf(input);
	 }
	 
	 
	 
	 
}//end class


