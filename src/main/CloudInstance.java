package main;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;
import java.util.stream.Collectors;

public class CloudInstance {
    Map<String, List<Server>> serverListOfAllRegion = new HashMap<>();
    Map<String, Integer> serverTypes = new HashMap<>();

    static void combinates(List<String> servers, List<String> data, int start, int end,
                           int index, int r, List<List<String>> serverCombinations) {
        if (index == r) {
            List<String> list = new ArrayList<>();
            for (int j = 0; j < r; j++) {
                list.add(data.get(j));
            }
            if (!list.isEmpty()) {
                serverCombinations.add(list);
            }
        }
        for (int i = start; i <= end && ((end - i + 1) >= (r - index)); i++) {
            data.add(index, servers.get(i));
            combinates(servers, data, i + 1, end, index + 1, r, serverCombinations);
        }
    }

//    public List<Instance> getServerList(Map<String, Integer> serverTypes) throws Throwable {
//        return null;
//    }
//
//    public Map<String, List<Server>> getServerDenominations(Map<String, Integer> serverTypes
//            , Map<String, Map<String, Float>> serversWithPrice) throws Throwable {
//        Map<String, List<Server>> serverDenominations = new HashMap<>();
//        List<Server> serverList = new ArrayList<>();
//        for (Map.Entry<String, Map<String, Float>> serverOrigin : serversWithPrice.entrySet()) {
//            Map<String, Float> serverPriceList = serverOrigin.getValue();
//            for (Map.Entry<String, Float> serverType : serverPriceList.entrySet()) {
//                Server server = new Server(serverOrigin.getKey(), serverType.getKey()
//                        , serverType.getValue(), serverTypes.get(serverType.getKey()));
//                serverList.add(server);
//            }
//            serverDenominations.put(serverOrigin.getKey(), serverList);
//        }
//        return serverDenominations;
//    }
//
//    public List<Instance> getInstancesWithMaxPrice(Map<String, Integer> serverTypes, Map<String
//            , List<Server>> ServerDenominations, Float maxPrice, int hours, int minimumCPUs) throws Throwable {
//        List<Instance> instanceListOfAllRegions = getInstanceList(ServerDenominations, maxPrice, hours, minimumCPUs, serverTypes);
//        instanceListOfAllRegions.sort((i1, i2) -> i1.total_cost.compareTo(i2.total_cost));
//        return instanceListOfAllRegions;
//    }

    public List<Map<String, Object>> get_costs(int hours, int minimumCPUs
            , float maximumPrice) throws Throwable {
        List<Instance> instanceList = getInstanceList(serverListOfAllRegion, maximumPrice
                                                            , hours, minimumCPUs, serverTypes);
        List<Map<String, Object>> list = new ObjectMapper()
                                            .convertValue(instanceList
                                                    , new TypeReference<List<Map<String, Object>>>() {});
        return list;
    }

    public List<Instance> getInstanceList(Map<String, List<Server>> serverListOfAllRegion
            , Float maxPrice, int hours, int minimumCPUs, Map<String, Integer> serverTypes) throws Throwable {
        List<Instance> instanceList = new ArrayList<>();
        for (Map.Entry<String, List<Server>> serverEntry : serverListOfAllRegion.entrySet()) {
            List<Server> servers = serverEntry.getValue();
            Map<String, Float> serverPrices = getServerPrice(servers);
            List<Instance> cheapServerInstances = getServerInstancesBasedOnUserWish(serverEntry.getKey()
                                                    , serverPrices, hours, minimumCPUs, maxPrice, serverTypes);
            instanceList.addAll(cheapServerInstances);
        }
        return instanceList;
    }

    public List<Instance> getServerInstancesBasedOnUserWish(String region, Map<String, Float> serverPrices, int hours
            , int minimumCPUs, float maxPrice, Map<String, Integer> serverTypes) throws Throwable {
        List<Instance> cheapServerInstance = new ArrayList<>();
        List<List<Tuple>> serverCombinationsWithCPUs = getServerCombinationWithCPUs(minimumCPUs, serverTypes);
        List<List<Tuple>> serverCombinationsWithPrice = getServerCombinationWithPrice(serverCombinationsWithCPUs
                                                                                                    , serverPrices);
        for (int i = 0; i < serverCombinationsWithPrice.size(); i++) {
            List<Tuple> serverWithCPUs = serverCombinationsWithCPUs.get(i);
            serverWithCPUs.sort((s1, s2) -> String.valueOf(s1.get(0)).compareTo(String.valueOf(s2.get(0))));
            List<Tuple> serversWithPrice = serverCombinationsWithPrice.get(i);
            Float totalPrice = getTotalPrice(serversWithPrice);
            if (totalPrice <= maxPrice) {
                Instance instance = new Instance(region, totalPrice, serverWithCPUs);
                cheapServerInstance.add(instance);
            }
        }
        cheapServerInstance.sort((i1, i2) -> i1.total_cost.compareTo(i2.total_cost));
        return cheapServerInstance;
    }

    public List<List<Tuple>> getServerCombinationWithPrice(List<List<Tuple>> serverCombinationsWithCPUs
                                                                        , Map<String, Float> serverPrices) {
        List<List<Tuple>> serverWithCPUPrices = new ArrayList<>();
        for (List<Tuple> serverWithCPUs : serverCombinationsWithCPUs) {
            List<Tuple> CPUList = new ArrayList<>();
            for (Tuple server : serverWithCPUs) {
                Tuple CPUsWithPrice = new PairTuple(server.get(0)
                                        , (Float) server.get(1) * serverPrices.get(server.get(0)));
                CPUList.add(CPUsWithPrice);
            }
            serverWithCPUPrices.add(CPUList);
        }
        return serverWithCPUPrices;
    }

    public List<List<Tuple>> getServerCombinationWithCPUs(int minimumCPUs
                                    , Map<String, Integer> serverTypes) throws Throwable {
        List<String> serverNames = serverTypes.keySet().stream().collect(Collectors.toList());
        List<List<String>> serverCombinations = getServerCombinations(serverNames);
        List<List<Tuple>> serverCombinationWithCPUs = new ArrayList<>();
        for (List<String> servers : serverCombinations) {
            List<Tuple> tupleList = getCPUsCombinations(servers, minimumCPUs, serverTypes);
            serverCombinationWithCPUs.add(tupleList);
        }
        return serverCombinationWithCPUs;
    }

    private List<Tuple> getCPUsCombinations(List<String> servers, int minimumCPUs
                                    , Map<String, Integer> serverTypes) throws Throwable {
        Map<Integer, String> serverWithCPUs = new TreeMap<>();
        for (String server : servers) {
            serverWithCPUs.put(serverTypes.get(server), server);
        }
        List<Tuple> tupleList = new ArrayList<>();
        int i = 0;
        for (Map.Entry<Integer, String> entry : serverWithCPUs.entrySet()) {
            int countIndex[] = new int[serverTypes.size()];
            for (int j = 0; j < servers.size(); j++) {
                Tuple tuple = null;
                if (j == 0) {
                    countIndex = initializeCPUcounts(countIndex, i, entry.getKey(), minimumCPUs);
                    tuple = new PairTuple(entry.getValue(), countIndex[i] - 1);
                } else {
                    tuple = new PairTuple(entry.getValue(), countIndex[i] - 1);
                }
                tupleList.add(tuple);
            }
            i++;
        }
        return tupleList;
    }

    public int[] initializeCPUcounts(int[] countIndex, int i, int cpuCount, int minimumCPUs) throws Throwable {
        int filledCPUs = 0;
        for (int k = 0; k < countIndex.length; k++) {
            if (k != i) {
                countIndex[k] = cpuCount;
                filledCPUs += cpuCount;
            }
        }
        countIndex[i] = minimumCPUs - filledCPUs;
        return countIndex;
    }

    public List<List<String>> getServerCombinations(List<String> serverTypes) throws Throwable {
        List<List<String>> serverCombinations = new ArrayList<>();
        int n = serverTypes.size();
        List<String> data = new ArrayList<>();
        for (int r = 0; r < serverTypes.size(); r++) {
            combinates(serverTypes, data, 0, n - 1, 0, r, serverCombinations);
        }
        List<String> finalCombination = new ArrayList<>();
        for (String serverType : serverTypes) {
            finalCombination.add(serverType);
        }
        serverCombinations.add(finalCombination);
        return serverCombinations;
    }

    public Float getTotalPrice(List<Tuple> serverTypes) throws Throwable {
        Float totalPrice = 0.0f;
        for (Tuple tuple : serverTypes) {
            totalPrice += (Float) tuple.get(1);
        }
        return totalPrice;
    }

    public Map<String, Float> getServerPrice(List<Server> servers) throws Throwable {
        Map<String, Float> serverPrices = new HashMap<>();
        for (Server server : servers) {
            serverPrices.put(server.type, server.getPrice());
        }
        return serverPrices;
    }

    public List<Map<String, Object>> getCheapServers(int hours, int minimumCPUs, float maxiPrice
            , Map<String, Integer> serverTypes, Map<String, Float> serverCost) throws Throwable {
        List<Map<String, Object>> listOfServers = new ArrayList<>();

        return null;
    }


    /*
    TreeMap<Float, List<PairTuple>> costOfServers = new TreeMap<>();
        List<PairTuple> serverDenominations = new ArrayList<>();
        for(Map.Entry<String, Integer> serverTypeEntry : serverTypes.entrySet()) {
            if(minimumCPUs / serverTypeEntry.getValue() == 0) {
                Integer numOfServers = serverTypeEntry.getValue();
                float totalcost = numOfServers * hours * serverCost.get(serverTypeEntry.getKey());
                PairTuple pairTuple = new PairTuple(serverTypeEntry.getKey(), numOfServers);
                serverDenominations.add(pairTuple);
                costOfServers.put(totalcost, serverDenominations);
            }
        }
        return (Map<String, Object>) costOfServers.firstEntry();
     */
}

