package com.eric.esext.rest;

import org.elasticsearch.action.admin.cluster.node.info.NodeInfo;
import org.elasticsearch.action.admin.cluster.node.info.NodesInfoResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.node.NodeClient;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.rest.*;
import org.elasticsearch.rest.action.RestBuilderListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 获取特定名称，或是包含特定前缀的节点名称列表
 * 使用方式： curl -XGET 'localhost:9200/_masting/nodes?prefix=test'
 */
public class CustomerRestAction extends BaseRestHandler {
    @Inject
    public CustomerRestAction(Settings settings, RestController restController, Client client) {
        super(settings);
        restController.registerHandler(RestRequest.Method.GET, "/_masting/nodes", this);
    }


    @Override
    protected RestChannelConsumer prepareRequest(RestRequest restRequest, NodeClient nodeClient) throws IOException {
        final String nodePrefix = restRequest.param("prefix", "");
//        return new RestChannelConsumer() {
//            @Override
//            public void accept(RestChannel channel) throws Exception {
//
//            }
//        }
        // 下面的代码类似于上面的效果
        return (channel) -> {

            nodeClient.admin().cluster().prepareNodesInfo().all().execute(new RestBuilderListener<NodesInfoResponse>(channel) {
                @Override
                public RestResponse buildResponse(NodesInfoResponse nodesInfoResponse, XContentBuilder xContentBuilder) throws Exception {
                    List<String> nodes = new ArrayList<String>();
                    for (NodeInfo nodeInfo : nodesInfoResponse.getNodes()) {
                        if (nodePrefix.isEmpty()) {
                            nodes.add(nodeInfo.getNode().getName());
                        } else if (nodeInfo.getNode().getName().startsWith(nodePrefix)) {
                            nodes.add(nodeInfo.getNode().getName());
                        }
                    }
                    xContentBuilder.startObject().field("nodes", nodes).endObject();
                    return new BytesRestResponse(RestStatus.OK, xContentBuilder);
                }
            });

        };
    }
}
