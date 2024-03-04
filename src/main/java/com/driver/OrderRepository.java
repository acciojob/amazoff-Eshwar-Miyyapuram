package com.driver;

import java.util.*;

import org.springframework.stereotype.Repository;

@Repository
public class OrderRepository {

    private HashMap<String, Order> orderMap;
    private HashMap<String, DeliveryPartner> partnerMap;
    private HashMap<String, HashSet<String>> partnerToOrderMap;
    private HashMap<String, String> orderToPartnerMap;

    public OrderRepository(){
        this.orderMap = new HashMap<String, Order>();
        this.partnerMap = new HashMap<String, DeliveryPartner>();
        this.partnerToOrderMap = new HashMap<String, HashSet<String>>();
        this.orderToPartnerMap = new HashMap<String, String>();
    }

    public void saveOrder(Order order){
        // your code here
        if(!orderMap.containsKey(order.getId())){
            orderMap.put(order.getId(), order);
        }
    }

    public void savePartner(String partnerId){
        // your code here
        // create a new partner with given partnerId and save it
        if(!partnerMap.containsKey(partnerId)){
            partnerMap.put(partnerId, new DeliveryPartner(partnerId));
            partnerToOrderMap.put(partnerId,new HashSet<String>());
        }
    }

    public void saveOrderPartnerMap(String orderId, String partnerId){
        if(orderMap.containsKey(orderId) && partnerMap.containsKey(partnerId)) {
            // your code here
            //add order to given partner's order list
            //increase order count of partner
            //assign partner to this order
            if (!orderToPartnerMap.containsKey(orderId)) {
                HashSet<String> orderList = partnerToOrderMap.get(partnerId);
                orderList.add(orderId);
                partnerToOrderMap.put(partnerId, orderList);
                DeliveryPartner partner = partnerMap.get(partnerId);
                partner.setNumberOfOrders(partner.getNumberOfOrders() + 1);
                orderToPartnerMap.put(orderId, partnerId);
            }
       }
    }

    public Order findOrderById(String orderId){
        // your code here
        Order order = orderMap.get(orderId);
        return order;
    }

    public DeliveryPartner findPartnerById(String partnerId){
        // your code here
        DeliveryPartner partner=partnerMap.get(partnerId);
        return partner;
    }
//
    public Integer findOrderCountByPartnerId(String partnerId){
        // your code here
        DeliveryPartner partner = partnerMap.get(partnerId);

        return partner.getNumberOfOrders();
    }

    public List<String> findOrdersByPartnerId(String partnerId){
        // your code here
        return new ArrayList(partnerToOrderMap.get(partnerId));
    }
//
    public List<String> findAllOrders(){
        // your code here
        // return list of all orders
        return new ArrayList<>(orderMap.keySet());
    }
//
    public void deletePartner(String partnerId){
        // your code here
        // delete partner by ID
        if(partnerMap.containsKey(partnerId)){
            partnerMap.remove(partnerId);
        }
        if(partnerToOrderMap.containsKey(partnerId)){
            HashSet<String> orderList = partnerToOrderMap.get(partnerId);
            partnerToOrderMap.remove(partnerId);
            for(String order:orderList){
                orderToPartnerMap.remove(order);
            }
        }
    }
//
    public void deleteOrder(String orderId){
        // your code here
        // delete order by ID
        if(orderMap.containsKey(orderId)){
            orderMap.remove(orderId);
        }
        if(orderToPartnerMap.containsKey(orderId)){
            String partnerId = orderToPartnerMap.get(orderId);
            orderToPartnerMap.remove(orderId);

            HashSet<String> orders=partnerToOrderMap.get(partnerId);
            orders.remove(orderId);
        }
    }
//
    public Integer findCountOfUnassignedOrders(){

        return orderMap.size()-orderToPartnerMap.size();
    }
//
    public Integer findOrdersLeftAfterGivenTimeByPartnerId(String timeString, String partnerId){
        int timeInMinutes= Integer.parseInt(timeString.substring(0,2))*60+Integer.valueOf(timeString.substring(3,5));

        List<String> ordersList=new ArrayList<>(partnerToOrderMap.get(partnerId));

        Integer count=0;
        for(String orderId:ordersList){
            if(orderMap.get(orderId).getDeliveryTime()>timeInMinutes){
                count++;
            }
        }
        return count;
    }
//
    public String findLastDeliveryTimeByPartnerId(String partnerId){
        // your code here
        // code should return string in format HH:MM
        HashSet<String> orderList=partnerToOrderMap.get(partnerId);

        int maxDelivery=-1;
        for(String order:orderList){
            if(maxDelivery<orderMap.get(order).getDeliveryTime()){
                maxDelivery=orderMap.get(order).getDeliveryTime();
            }
        }
        int minutes=maxDelivery%60;
        int hours=maxDelivery/60;
        if(hours<10 && minutes<10){
            return "0"+hours+":"+"0"+minutes;
        }
        else if(hours<10){
           return "0"+hours+":"+minutes;
        }
        else if(minutes<10){
            return hours+":"+"0"+minutes;
        }
        return hours+":"+minutes;
    }
}