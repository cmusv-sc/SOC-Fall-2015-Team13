/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package controllers;

import models.User;
import models.UserRepository;
import play.mvc.Controller;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import java.util.regex.Pattern;
import java.util.*;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;

import org.apache.spark.mllib.clustering.KMeans;
import org.apache.spark.mllib.clustering.KMeansModel;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

@Named
@Singleton
public class MLController extends Controller {
    private final UserRepository userRepository;

    private ScheduledExecutorService scheduler = new ScheduledThreadPoolExecutor(5);

    private static class ParsePoint implements Function<String, Vector> {
        private static final Pattern SPACE = Pattern.compile(" ");

        @Override
        public Vector call(String line) {
            String[] tok = SPACE.split(line);
            double[] point = new double[tok.length];
            for (int i = 0; i < tok.length; ++i) {
                point[i] = Double.parseDouble(tok[i]);
            }
            return Vectors.dense(point);
        }
    }

    @Inject
    public MLController(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public void start() {
        Runnable r = new Runnable() {
            public void run() {
                long currentTime = System.currentTimeMillis();
                Iterable<User> userIterable = userRepository.findAll();
                List<User> userList = new ArrayList<User>();
                for (User user : userIterable) {
                    userList.add(user);
                }

                List<String> source = new ArrayList<String>();
                for (User user : userList) {
                    String research  = user.getResearchFields();
                    String title = user.getTitle();
                    double researchScore = 10.0;
                    if (research != null && !research.isEmpty() &&
                            (research.toLowerCase().contains("web") || research.toLowerCase().contains("data")
                                    || research.toLowerCase().contains("machine"))) {
                        researchScore = 0.0;
                    }
                    double titleScore = 10.0;
                    if ( title != null && !title.isEmpty()) {
                        if (title.toLowerCase().contains("master") || title.toLowerCase().contains("dr")) {
                            titleScore = 0.0;
                        }
                    }

                    String rawScore = "" + user.getId() + " " + researchScore + " " + titleScore;
                    source.add(rawScore);

                }

                JavaSparkContext sc = new JavaSparkContext("local", "JavaKMeans");
                JavaRDD<String> lines = sc.parallelize(source);

                JavaRDD<Vector> points = lines.map(new ParsePoint());

                KMeansModel model = KMeans.train(points.rdd(), 2, 10, 1, KMeans.K_MEANS_PARALLEL());

                Vector center0 = model.clusterCenters()[0];
                Vector center1 = model.clusterCenters()[1];

                // Compute which cluster the user belongs to
                Pattern sp = Pattern.compile(" ");
                for (String rawData : source) {
                    String[] tok = sp.split(rawData);
                    double[] point = new double[tok.length];
                    for (int i = 0; i < tok.length; ++i) {
                        point[i] = Double.parseDouble(tok[i]);
                    }

                    Vector currentVector = Vectors.dense(point);
                    User user = userRepository.findOne((long) point[0]);
                    if (Vectors.sqdist(currentVector, center0) >= Vectors.sqdist(currentVector, center1)) {
                        user.setCluster(0);
                    }
                    else {
                        user.setCluster(1);
                    }

                    userRepository.save(user);

                }

                sc.stop();
            }
        };
        scheduler.scheduleWithFixedDelay(r, 0, 5 * 60 * 1000,
                TimeUnit.MILLISECONDS);
    }
}
