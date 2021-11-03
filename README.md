# Initial Application

```bash
$ mvn clean package
$ mvn package azure-webapp:deploy
```

- Application Gateway 接続文字列の環境変数設定？
```bash
$ export CONNECTION_STRING=InstrumentationKey=a4cfcf45-6c12-4711-8eab-3acad8c20f7f;IngestionEndpoint=https://westus2-2.in.applicationinsights.azure.com/
```

## Reference

- [Azure SQL Database で Java と JDBC を使用する](https://docs.microsoft.com/ja-jp/azure/azure-sql/database/connect-query-java)
- [クイック スタート:Azure App Service で Java アプリを作成する](https://docs.microsoft.com/ja-jp/azure/app-service/quickstart-java?tabs=javase&pivots=platform-linux)
- [構成オプション - Azure Monitor Application Insights for Java](https://docs.microsoft.com/ja-jp/azure/azure-monitor/app/java-standalone-config)
- [Web ページ向けの Application Insights](https://docs.microsoft.com/ja-jp/azure/azure-monitor/app/javascript)