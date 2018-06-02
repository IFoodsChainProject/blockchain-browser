package com.ifoods.controller;

import com.ifoods.model.Address;
import com.ifoods.model.AddressTransaction;
import com.ifoods.model.Block;
import com.ifoods.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 
 */
@Controller
public class IndexController
{
    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${pageSize}")
    private Integer pageSize;

    private final static Logger logger = LoggerFactory.getLogger(IndexController.class);

    /**
     * 首页
     */
    @GetMapping("/")
    public String index(Model model) {
        return "index";
    }

    /**
     * 区块信息分页展示
     */
    @GetMapping("/block")
    public String block(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, Model model)
    {
        model.addAttribute("currentPage", pageNumber);

        Query query = new Query().with(new Sort(Sort.Direction.DESC, "_id"));
        long count = mongoTemplate.count(query, Block.class);
        long pages = count % 50 == 0 ? count/50 : count/50 + 1;
        model.addAttribute("pages", pages);

        query.skip((pageNumber-1)*pageSize).limit(pageSize);
        List<Block> blocks = mongoTemplate.find(query, Block.class);
        model.addAttribute("blocks", blocks);

        return "block";
    }

    /**
     * 区块明细展示
     */
    @GetMapping("/block/{blockId}")
    public String blockDetail(@PathVariable("blockId") Long blockId, Model model)
    {
        Block block = mongoTemplate.findById(blockId, Block.class);
        if(null == block)
        {
            return "404";
        }
        model.addAttribute("block", block);

        return "blockDetail";
    }

    /**
     * 交易信息分页展示
     */
    @GetMapping("/tx")
    public String tx(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, Model model)
    {
        Integer pageSize = 10;

        model.addAttribute("currentPage", pageNumber);

        Query query = new Query(Criteria.where("txType").ne(0)).with(new Sort(Sort.Direction.DESC, "timestamp"));
        long count = mongoTemplate.count(query, Transaction.class);
        long pages = count % pageSize == 0 ? count/pageSize : count/pageSize + 1;
        model.addAttribute("pages", pages);

        query.skip((pageNumber-1)*pageSize).limit(pageSize);
        List<Transaction> transactions = mongoTemplate.find(query, Transaction.class);
        model.addAttribute("transactions", transactions);

        return "tx";
    }

    /**
     * 交易明细展示
     */
    @GetMapping("/tx/{hash}")
    public String txDetail(@PathVariable("hash") String hash, Model model)
    {
        Transaction transaction = mongoTemplate.findById(hash, Transaction.class);
        if(null == transaction)
        {
            return "404";
        }
        model.addAttribute("transaction", transaction);

        return "txDetail";
    }

    /**
     * 地址信息分页展示
     */
    @GetMapping("/address")
    public String address(@RequestParam(value = "pageNumber", required = false, defaultValue = "1") Integer pageNumber, Model model)
    {
        model.addAttribute("currentPage", pageNumber);

        Query query = new Query().with(new Sort(Sort.Direction.DESC, "lastTime"));
        long count = mongoTemplate.count(query, Address.class);
        long pages = count % 50 == 0 ? count/50 : count/50 + 1;
        model.addAttribute("pages", pages);

        query.skip((pageNumber-1)*pageSize).limit(pageSize);
        List<Address> addresses = mongoTemplate.find(query, Address.class);
        model.addAttribute("addresses", addresses);

        return "address";
    }

    /**
     * 地址明细展示
     */
    @GetMapping("/address/{id}")
    public String addressDetail(@PathVariable("id") String id, Model model)
    {
        Address address = mongoTemplate.findById(id, Address.class);
        if(null == address)
        {
            return "404";
        }

        //查询关联的交易
        Pattern pattern = Pattern.compile("^.*"+ id +".*$", Pattern.CASE_INSENSITIVE);
        Query query = new Query(Criteria.where("_id").regex(pattern)).with(new Sort(Sort.Direction.DESC, "transactionTime"));
        List<AddressTransaction> addressTransactions = mongoTemplate.find(query, AddressTransaction.class);
        List<Transaction> transactions = new ArrayList<>();
        for(AddressTransaction addressTransaction: addressTransactions)
        {
            transactions.add(addressTransaction.getTransaction());
        }
        address.setTransactions(transactions);
        model.addAttribute("address", address);
        return "addressDetail";
    }

    /**
     * 根据区块高度、区块Hash，交易Hash，地址来查询
     */
    @PostMapping("/search")
    public String search(@RequestParam(value = "value", required = false, defaultValue = "") String value, Model model)
    {
        Query query = null;

        //判断是否是区块高度
        Long blockId = null;
        try
        {
            blockId = Long.parseLong(value);
            query = new Query(Criteria.where("_id").is(blockId));
            Block block = mongoTemplate.findOne(query, Block.class);
            if (null != block)
            {
                model.addAttribute("block", block);
                return "blockDetail";
            }
        }
        catch (NumberFormatException e)
        {

        }

        //判断是否区块Hash
        query = new Query(Criteria.where("hash").is(value));
        Block block = mongoTemplate.findOne(query, Block.class);
        if (null != block)
        {
            model.addAttribute("block", block);
            return "blockDetail";
        }

        //判断是否是交易Hash
        query = new Query(Criteria.where("_id").is(value));
        Transaction transaction = mongoTemplate.findOne(query, Transaction.class);
        if (null != transaction)
        {
            model.addAttribute("transaction", transaction);
            return "txDetail";
        }

        //判断是否是地址
        query = new Query(Criteria.where("_id").is(value));
        Address address = mongoTemplate.findOne(query, Address.class);
        if (null != address)
        {
            //查询关联的交易
            Pattern pattern = Pattern.compile("^.*"+ value +".*$", Pattern.CASE_INSENSITIVE);
            query = new Query(Criteria.where("_id").regex(pattern)).with(new Sort(Sort.Direction.DESC, "transactionTime"));
            List<AddressTransaction> addressTransactions = mongoTemplate.find(query, AddressTransaction.class);
            List<Transaction> transactions = new ArrayList<>();
            for(AddressTransaction addressTransaction: addressTransactions)
            {
                transactions.add(addressTransaction.getTransaction());
            }
            address.setTransactions(transactions);
            model.addAttribute("address", address);
            return "addressDetail";
        }

        return "404";
    }
}
