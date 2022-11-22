import java.awt.event.ActionEvent;

//ACTION//
public class Capture_GUI {
private boolean CaptureState;

public void Action_B_CAPTURE(ActionEvent X){
	TA_OUTPUT.setText("");
	CaptureState=true;
	CapturePackets();
}

public void Action_B_LIST(ActionEvent X){
	ListNetworkInterfaces( );
	TF_SelectInterface.requestFocus() ;
}

public void Action_B_SELECT(ActionEvent X){
	ChooseInterface() ;
}

public void Action_B_STOP(ActionEvent X){
	CaptureState = false;
	CAPTAIN.finished() ;
}

public void Action_B_PORT(ActionEvent X)
{
	PortScanner();
}

public void Action_B_SAVE(ActionEvent X)
{
	//CaptureData();
	SaveCapture();
}

public void Action_B_SAVELOCAL(ActionEvent X){
	//CaptureData() ;
	CaptureDatalocal();
}

public void Action_B_PORTBLOCK(ActionEvent X)
{
	BlockPortsytem();
}

public void Action_B_PORTUNBLOCK(ActionEvent X) {
	UNBlockPortSytem();
}

public void Action_B_HONEYSTART(ActionEvent X) throws IOException
{
	HONEYSTART() ;
}

public void Action_B_HONEYSTOP(ActionEvent X) throws IOException
{
	//new HoneypotServer() .StopServer();
}

public void Action_B_HONEYSTARTIRC(ActionEvent X) throws IOException
{
	HONEYSTARTIRC();
}

public void Action_B_HONEYSTOPIRC(ActionEvent X)
{
	
}


//FUNCTIONS//

public void ListNetworkInterfaces(){

	try{
	network_interface = JpcapCaptor.getDevicelist();
	
	TA_OUTPUT.setText("");
		for(int i=0; i< network_interface.length;i++){
		
			TA_OUTPUT.append("\n----------------------------------Interface"+i+
					          "Info----------------------------------");
			TA_OUTPUT.append("\nInterface Number: "+i);
			TA_OUTPUT.append("\nDescription :"+network_interface[i].name+"("+
		                     network_interface[i].description);
			TA_OUTPUT.append("\nDataLink Name"+network_interface[i].datalink_name+"("+
					         network_interface[i].datalink_description+")");
		
			TA_OUTPUT.append("\nIP Address1 : "+INT.address);
			TA_OUTPUT.append("\nSubnet : "+INT. subnet);
		
			ADDRE = INT.address.toString();
			
			System.out.println(ADDRE);
			new_ip = ADDRE.replaceAll("/","");
			System.out.println(new_ip);
		}
		COUNTER++;
	}
}
	catch(Exception e){System.out.println(e);}}
//-----------------------------------------------------//

public void ChooseInterface() {
	int Temp = Integer.parseInt(TF_SelectInterface.getText());
		if(Temp > -1 && Temp < COUNTER) {
			INDEX= Temp;
			//EnableButtons();
		}
		else {
		
		}
	TF_SelectInterface.setText("");
}

//-----------------------------------------------------//
public void CapturePackets()
{
	CAPTAIN=new CaptureThread() {
	
		@Override
		public Object construct() {
		
		TA_OUTPUT.setText("\nNow capturing on interface :"+INDEX+"... "+
	                      "\n----------------------------------"
				           +"----------------------------------\n\n");
			try {
				CAP = JpcapCaptor. openDevice (network_interface[INDEX],65535, false, 20);
				//CAP.setFilter("ip", true);
					while(CaptureState) {
					CAP.processPacket(1, new PacketContents());
					}
				
				CAP.close();
			} catch (Exception e) {
				// TODO: handle exception
				System.out .println(e);
			}
			return 0;
		}
	public void finished(){
		this.interrupt();
	}
	};
			CAPTAIN.start();
}

//-----------------------------------------------------//
public void PortScanner() {

	try {
	
		string command = "netstat -a";
		System.out.println(command) ;
		string line;
		Process p = Runtime.getRuntime().exec(command) ;
		BufferedReader bri = new BufferedReader
		  (new InputStreamReader(p.getInputStream())) ;
		BufferedReader bre = new BufferedReader
		  (new InputStreamReader(p.getErrorStream())) ;
		while ((line = bri.readLine()) != null) {
			TA_PORT.append (line+"\n") ;
		}
		bri.close();
		while ((line = bre.readLine()) != null) {
			TA_PORT.append(line+"\n");
		}
		bre.close();
		p.waitFor();
	}
	catch (Exception err) {
		err.printStackTrace();
	}
}

//-----------------------------------------------------//
public void SaveCapture()
{
	Thread t1 = new Thread( new Runnable(){
		@Override

		public void run() {
			// TODO Auto-generated method stub

			for (int i=1; i <=3; i++) {
				System.out.println("Saving from SaveThread class..... ");
				CaptureData();
				try{
					Thread.sleep(10000) ;
				}catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
	});
	t1.start();
}

//-----------------------------------------------------//
public void BlockPortSytem()
{

	String num = TF_PortBlock.getText().toString();
	String protocol = (String) comboBox.getSelectedItem();
	
	BlockPort block = new BlockPort(num, protocol) ;
}
// UNBLOCK FUNCTION//

public void UNBlockPortSytem() {
	String num = TF_PortUNBlock.getText().toString();
	String protocol = (String) comboBox_UN.getSelectedItem();

	UNBlock unblock = new UNBlock(num, protocol) ;
}
//HONETPOT-on FTP//


public void HONEYSTART()
{

	honeyftp = new Thread(new Runnable() {
	
		@Override
		public void run() {
			// TODO Auto-generated method stub
			
			try {
				new HoneypotServer().runServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	});
	honeyftp.start();
	// HoneypotServer h1 = new HoneypotServer();
	JOptionPane. showMessageDialog(MainWindow,"HoneyServer Started");
}

//HONETPOT-on IRC//
public void HONEYSTARTIRC()
{

	Thread t2 = new Thread(new Runnable() {
		@Override
		public void run() {
		// TODO Auto-generated method stub
		
			try {
				new HoneypotServerIRC().runServer();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	});
	t2.start();
	JOptionPane. showMessageDialog(MainWindow, "HoneyServerIRC Started");
}

		 

public static void CaptureData()
{
	String CaptureData=TA_OUTPUT.getText();
	
	try {
	
		MongoClient mongoclient = new MongoClient("localhost",27017);
		DB db = mongoclient.getDB( " tep" );
		System.out.println("Connect to database successfully");
		
		/* a file into mongo db using grid fs * */
		DBCollection coll = db.getCollection("mycol");
		
		System.out.println("capture data" + CaptureData);
		BasicDBObject document = new BasicDBObject();
		document. put("first", CaptureData);
		
		coll.insert (document) ;
		System. out .println("DONE");
	} catch (Exception e) {
		// T000: handle exception
		e.printStackTrace();
	}
}

// saving data on local machine

public static void CaptureDataLocal()
{

	String CaptureData=TA_OUTPUT. getText();
	try {File Data = new File(DEFAULT_LOG_DIR,+new Date().getDate()+". log");
	
		FileOutputStream datastream = new FileOutputStream(Data);
		PrintStream out = new PrintStream(datastream);
		out. print(CaptureData) ;
		out.close();
		datastream.close();
		System.out.println( "Saving........from CaptureData function");
	} catch (Exception e) {
		// TODO: handle exception
		e.printStackTrace();
	}
}

//data in xml formate
public static void CaptureDataXML() 
{
	String CaptureData=TA_OUTPUT.getText();

	try {
		File Data = new File("OutPut.txt");
		FileOutputStream datastream = new FileOutputStream(Data);
		PrintStream out = new PrintStream(datastream) ;
		out .print(CaptureData) ;
	
		out .close();
		datastream.close();
		System. out .println("Saving........from CaptureData function");

	} catch (Exception e) {
		//TOD0: handle exception
		e.printstackTrace();
	}
}

//no of connection//
public void setNumberConnections(int newNum) {
	System. out.println("reached here");
	
	L_HACKERCONNECTED. setText("Hackers connected: " + newNum);
}

public void setNumberConnectionsIRC(int newNum) {
	System.out.println("reached here");

	L_HACKERCONNECTED1.setText("Hackers connected: " + newNum);
}
}