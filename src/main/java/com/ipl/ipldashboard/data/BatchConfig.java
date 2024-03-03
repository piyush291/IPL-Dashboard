package com.ipl.ipldashboard.data;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
// import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
// import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import com.ipl.ipldashboard.model.Match;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final String[] FIELD_NAMES = new String[] {
            "ID", "City", "Date", "Season", "MatchNumber", "Team1", "Team2", "Venue", "TossWinner", "TossDecision",
            "SuperOver", "WinningTeam", "WonBy", "Margin", "method", "Player_of_Match", "Team1Players", "Team2Players",
            "Umpire1", "Umpire2"
    };

    // @Autowired
    // public JobBuilderFactory jobBuilderFactory;

    // @Autowired
    // public StepBuilderFactory stepBuilderFactory;

    @Bean
    public FlatFileItemReader<MatchInput> reader() {
        System.out.println("########Piyush33333333###########");
        return new FlatFileItemReaderBuilder<MatchInput>()
                .name("MatchItemReader")
                .resource(new ClassPathResource("match-data.csv"))
                .delimited()
                .names(FIELD_NAMES)
                .targetType(MatchInput.class)
                .build();
    }

    @Bean
    public MatchDataProcessor processor() {
        System.out.println("########Piyush555555555###########");
        return new MatchDataProcessor();
    }

    @Bean
    public JdbcBatchItemWriter<Match> writer(DataSource dataSource) {
        System.out.println("########Piyush444444###########");
        return new JdbcBatchItemWriterBuilder<Match>()
                .sql("INSERT INTO match (ID, City, Date, Venue, Player_of_match, Team1, Team2, Toss_decision, Toss_winner, Margin, Umpire1, Umpire2) "
                        + " VALUES (:ID, :City, :Date, :Venue, :PlayerOfMatch, :Team1, :Team2, :TossDecision, :TossWinner, :Margin, :Umpire1, :Umpire2)")
                .dataSource(dataSource)
                .beanMapped()
                .build();
    }

    @Bean
    public Job importUserJob(JobRepository jobRepository, Step step1, JobCompletionNotificationListener listener) {
        System.out.println("########Piyush2222###########");
        return new JobBuilder("importUserJob", jobRepository)
                .listener(listener)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
            FlatFileItemReader<MatchInput> reader, MatchDataProcessor processor, JdbcBatchItemWriter<Match> writer) {
        System.out.println("########Piyush6666666666###########");
        return new StepBuilder("step1", jobRepository)
                .<MatchInput, Match>chunk(3, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

//     @Bean
//     public Step step1(JdbcBatchItemWriter<Match> writer) {
//         return StepBuilderFactory
//                 .get("step1")
//                 .<MatchInput, Match>chunk(3)
//                 .reader(reader())
//                 .processor(processor())
//                 .writer(writer)
//                 .build();
//     }

//     @Bean
//       public DataSourceTransactionManager transactionManager(){
//         DataSourceTransactionManager transactionManager = new DataSourceTransactionManager();
//         //  dataSource.setDriverClassName("com.mysql.jdbc.Driver");
//         //  dataSource.setUrl("jdbc:mysql://localhost:3306/myDB");
//         //  dataSource.setUsername( "root" );
//         //  dataSource.setPassword( "" );
//          return transactionManager;
//       }

}
