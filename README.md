# Kafka Message Tool
Simple GUI tool (javaFX) to facilitate sending/receiving messages to/from kafka broker

**Features**
 - Display kafka broker configuration for each node in cluster
 - Show partition assignments to consumers
 - Send multi-messages to kafka scripted content (groovy)
 - Send messages (ProducerRecord) with user provided key
 - Find witch topic partition the message will be assigned to
 - Each send/receive window can be detached 

**Example screenshots**
- ![BrokerView](md.resources/broker_view_01.PNG "Broker view")
- ![BrokerView](md.resources/broker_view_02.PNG "Topic detail view")

**Build**  
- To build the app just execute `build` gradle task. There should be `kafka-message-tool<version>.jar` file created in 
`<project_dir>/build/libs` directory

**Run**  
 - To run the app just call`java -jar kafka-message-tool-<version>.jar`
 
**Credits**
 - Thanks to [RichtextFX](https://github.com/TomasMikula/RichTextFX) for providing cool CodeArea gui component 
 - Message syntax highlighting patterns for JSON were taken from [JFXParser](https://github.com/notnotme/JFXParser)
 - Thanks to [this page](http://respostas.guj.com.br/47439-habilitar-copypaste-tableview-funcionando-duvida-editar-funcionalidade) for copy TableView cell content code
   



