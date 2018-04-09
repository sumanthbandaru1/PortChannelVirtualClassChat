# PortChannelVirtualClassChat
In a virtual classroom, a teacher listen to every student’s message while he/she is lecturing (broadcasting messages to every student). In a more realistic classroom, a student needs to request speaking (to raise hand) and then speak upon the teacher’s approval. A student speaks to the whole class rather than to the teacher only. 

Package port_channel have two classes, ChannelPort and ChannelEndPoint. Both  have send and receive methods. ChannelPort has additional broadcast method. Operation receive is synchronous (blocking); operations send and broadcast are asynchronous. Since a ChannelPort can have multiple asynchronous senders, the receive method of ChannelPort is able to get any message from any sender in unpredictable order.

Java socket and JavaNIO are used for the port channel model.
