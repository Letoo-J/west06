package com.mine.west.userBasedCollaborativeFiltering;

import com.mine.west.models.Accountoperation;
import com.mine.west.models.Blog;

import java.util.*;

/**
 * 基于用户的协同过滤算法
 */
public class UserCF {
    private static final int MAX_ACCOUNT_NUMBER = 10000 + 5;

    private static List<Blog> baseBlog;
    private static List<Accountoperation> accountoperationList;

    //博客ID映射
    private static HashMap<Integer, Blog> IDBlog = new HashMap<>();
    //博客所对应的用户集
    private static HashMap<Integer, HashSet<Integer>> blogAccountCollection = new HashMap<>();
    //用户所对应的博客集
    private static HashMap<Integer, TreeSet<AccountInterest>> accountBlogCollection = new HashMap<>();
    //用户所对应博客数量
    private static HashMap<Integer, Integer> accountBlogSize = new HashMap<>();
    //用户相似度矩阵
    private static int[][] similarityMatrix = new int[MAX_ACCOUNT_NUMBER][MAX_ACCOUNT_NUMBER];

    public UserCF(List<Blog> baseBlog, List<Accountoperation> accountoperationList) {
        this.baseBlog = baseBlog;
        this.accountoperationList = accountoperationList;
    }

    public static List<Blog> getResult(Integer accountID) {
        if (baseBlog == null || baseBlog.size() < 1)
            return baseBlog;
        if (accountoperationList == null || accountoperationList.size() < 1)
            return baseBlog;

        for (Accountoperation ao : accountoperationList) {
            HashSet<Integer> as = blogAccountCollection.get(ao.getBlogID());
            if (as == null)
                as = new HashSet<>();
            as.add(ao.getAccountID());
            blogAccountCollection.put(ao.getBlogID(), as);

            TreeSet<AccountInterest> ais = accountBlogCollection.get(ao.getAccountID());
            if (ais == null)
                ais = new TreeSet<>();
            ais.add(new AccountInterest(ao.getBlogID(), ao.getInterest()));
            accountBlogCollection.put(ao.getAccountID(), ais);

            Integer num = accountBlogSize.get(ao.getAccountID());
            if (num == null)
                num = 0;
            accountBlogSize.put(ao.getAccountID(), num + 1);
        }

        for (Blog b : baseBlog)
            IDBlog.put(b.getAccountID(), b);

        Queue<SimilarAccount> accountList = getSimilarUsers(accountID);
        if (accountList == null || accountList.size() < 1)
            return baseBlog;

        List<Blog> ans = getAns(accountList, accountID);
        if (ans == null || ans.size() < 1)
            return baseBlog;

        return ans;
    }

    public static List<Blog> getAns(Queue<SimilarAccount> similarAccountQueue, Integer accountID) {
        List<Blog> ans = new ArrayList<>();

        TreeSet<AccountInterest> ait = accountBlogCollection.get(accountID);
        while (!similarAccountQueue.isEmpty()) {
            Integer similarID = similarAccountQueue.poll().getAccountID();
            TreeSet<AccountInterest> siat = accountBlogCollection.get(similarID);
            for (AccountInterest ai : siat) {
                if (ait.contains(ai))
                    continue;
                ans.add(IDBlog.get(ai.getBlogID()));
            }
        }

        return ans;
    }

    public static Queue<SimilarAccount> getSimilarUsers(Integer accountID) {
        Integer anum = accountBlogSize.get(accountID);
        if ((anum == null) || (anum < 1))
            return null;

        Iterator<Map.Entry<Integer, HashSet<Integer>>> iterator = blogAccountCollection.entrySet().iterator();
        while (iterator.hasNext()) {
            HashSet<Integer> as = iterator.next().getValue();
            for (Integer accO : as) {
                for (Integer accT : as) {
                    if (accO.equals(accT))
                        continue;
                    similarityMatrix[accO][accT]++;
                    similarityMatrix[accT][accO]++;
                }
            }
        }

        Queue<SimilarAccount> similar = new PriorityQueue<>((o1, o2) -> (int) (o2.getSimilar() - o1.getSimilar()));
        int accountNum = accountBlogSize.get(accountID);
        for (int i = 0; i < similarityMatrix.length; ++i) {
            if (!accountBlogSize.containsKey(i))
                continue;
            int num = accountBlogSize.get(i);
            if ((i != accountID) && (num < 1))
                similar.add(new SimilarAccount(i, (float) (similarityMatrix[accountID][i] / Math.sqrt(num * accountNum))));
        }

        return similar;
    }
}
