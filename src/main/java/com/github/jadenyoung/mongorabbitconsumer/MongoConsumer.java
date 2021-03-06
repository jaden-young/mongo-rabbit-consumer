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

import com.mongodb.client.MongoCollection;
import com.rabbitmq.client.*;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MongoConsumer extends DefaultConsumer{
    private final MongoCollection<Document> collection;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public MongoConsumer(Channel channel, MongoCollection<Document> collection) {
        super(channel);
        this.collection = collection;
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) {
        String json = new String(body);
        log.info("Received " + json);
        Document doc = Document.parse(json);
        collection.insertOne(doc);
    }
}
