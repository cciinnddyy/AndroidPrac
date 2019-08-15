package com.goyourlife.gofit_demo;

import com.microsoft.azure.storage.table.*;
import com.microsoft.azure.storage.CloudStorageAccount;
import com.microsoft.azure.storage.StorageException;

import com.microsoft.azure.storage.table.TableQuery.QueryComparisons;
import java.io.IOException;
public class ReceiveMessage {
    protected static String storageConnectionString =
            "DefaultEndpointsProtocol=https;" +
                    "AccountName=ifn701health;" +
                    "AccountKey=mpoMtocSl6vw1ceiVDi+SQJwvIN/86XYnwiiGuBGBg4R/oLwrVL18pgFrm5+6cZ4jqVWmq4hX2mOE+mrMoZhTg==";

    protected static CloudTableClient tableClient;
    protected static CloudTable table;
    protected final static String tableName = "Messages";
public ReceiveMessage() {
    try
    {
    CloudStorageAccount account = CloudStorageAccount.parse(storageConnectionString);
    tableClient = account.createCloudTableClient();
    table = tableClient.getTableReference("Message");
    table.createIfNotExists();


    }
    catch(Exception e){


    }
}
}
