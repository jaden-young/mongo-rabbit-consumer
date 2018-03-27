/*
 * Copyright 2018 Jaden Young
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.github.jadenyoung.mongorabbitconsumer;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.rabbitmq.client.*;
import org.bson.Document;

public class Main {

    public static void main(String[] argv) throws Exception {
        String mongoURI = System.getenv("MONGO_URI");
        String mongoDatabase = System.getenv("MONGO_DATABASE");
        String mongoCollection = System.getenv("MONGO_COLLECTION");
        String rabbitQueueName = System.getenv("RABBIT_QUEUE_NAME");
        String rabbitURI = System.getenv("RABBIT_URI");

        MongoClientURI connectionString = new MongoClientURI(mongoURI);
        MongoClient mongo = new MongoClient(connectionString);
        MongoDatabase db = mongo.getDatabase(mongoDatabase);
        MongoCollection<Document> collection = db.getCollection(mongoCollection);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setUri(rabbitURI);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(rabbitQueueName, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        MongoConsumer mongoConsumer = new MongoConsumer(channel, collection);
        channel.basicConsume(rabbitQueueName, true, mongoConsumer);
    }
}
