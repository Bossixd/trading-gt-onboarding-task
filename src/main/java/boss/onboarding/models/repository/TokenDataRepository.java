package boss.onboarding.models.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import boss.onboarding.models.entity.TokenData;

public class TokenDataRepository {
    private static final TokenDataRepository INSTANCE = new TokenDataRepository();
    private ConcurrentHashMap<String, TokenDataWrapper> tokenDataMap = new ConcurrentHashMap<>();
    private HashMap<String, ReentrantReadWriteLock> tokenLockMap = new HashMap<>();

    private TokenDataRepository() {
        System.out.println("Singleton created");
    }

    public static TokenDataRepository getDataSingleton() {
        return INSTANCE;
    }

    public int getDataLength(String token) {
        ReentrantReadWriteLock lock = tokenLockMap.getOrDefault(token, new ReentrantReadWriteLock());

        lock.readLock().lock();
        try {
            return tokenDataMap.get(token).size();
        } finally {
            lock.readLock().unlock();
        }
    }

    public void write(String token, Double value) {
        ReentrantReadWriteLock lock = tokenLockMap.getOrDefault(token, new ReentrantReadWriteLock());
        
        lock.writeLock().lock();
        try {
            tokenDataMap.putIfAbsent(token, new TokenDataWrapper());
            tokenDataMap.get(token).add(value);
            System.out.println(tokenDataMap.get(token));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public double read(String token, int index) {
        ReentrantReadWriteLock lock = tokenLockMap.getOrDefault(token, new ReentrantReadWriteLock());

        lock.readLock().lock();
        try {
            if (tokenDataMap.get(token) == null) {
                throw new IllegalArgumentException("This token has no data!");
            }

            return tokenDataMap.get(token).getDataIndex(index);
        } finally {
            lock.readLock().unlock();
        }
    }

    public ArrayList<Double> readAll(String token) {
        ReentrantReadWriteLock lock = tokenLockMap.getOrDefault(token, new ReentrantReadWriteLock());
        
        lock.readLock().lock();
        try {
            return tokenDataMap.get(token).getData();
        } finally {
            lock.readLock().unlock();
        }
    }

    public double getMax(String token) {
        ReentrantReadWriteLock lock = tokenLockMap.getOrDefault(token, new ReentrantReadWriteLock());
        
        lock.readLock().lock();
        try {
            return tokenDataMap.get(token).getMax();
        } finally {
            lock.readLock().unlock();
        }
    }

    public double getMin(String token) {
        ReentrantReadWriteLock lock = tokenLockMap.getOrDefault(token, new ReentrantReadWriteLock());
        
        lock.readLock().lock();
        try {
            return tokenDataMap.get(token).getMin();
        } finally {
            lock.readLock().unlock();
        }
    }

    private class TokenDataWrapper {
        private ArrayList<TokenData> dataArray = new ArrayList<>();
        private Double max;
        private Double min;

        public TokenDataWrapper() {
            max = null;
            min = null;
        }

        public void add(double value) {
            dataArray.add(new TokenData(value));
            if (max == null || value > max) {
                max = value;
            }
            if (min == null || value < min) {
                min = value;
            }
        }

        public int size() { return dataArray.size(); }
        public double getMax() { 
            if (max == null) {
                throw new IllegalArgumentException("This token has no data!");
            }

            return max;
        }

        public double getMin() { 
            if (min == null) {
                throw new IllegalArgumentException("This token has no data!");
            }
            return min;
        }

        public ArrayList<Double> getData() {
            ArrayList<Double> result = new ArrayList<>(dataArray.size());
            dataArray.forEach(d -> result.add(d.getData()));
            return result;
        }

        public double getDataIndex(int index) {
            if (index >= dataArray.size()) {
                throw new IllegalArgumentException("Index larger than array size!");
            }
            return dataArray.get(index).getData();
        }
    }
}
