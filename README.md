# ZBST search

Repo to implement basic upload and search functionality

###ELASTIC SEARCH:
* [JSON Upload](https://www.elastic.co/guide/en/kibana/current/tutorial-load-dataset.html)
* [Bulk API](https://www.elastic.co/guide/en/elasticsearch/reference/current/docs-bulk.html)

# Run locally:

- Run `docker run -p 9200:9200 docker.elastic.co/elasticsearch/elasticsearch:6.3.2`
- Run play app in IDEA OR run `sbt run`
- Run to upload data into ElasticSearch (change into work dir):

        curl -H 'Content-Type: application/x-ndjson' -XPOST 'localhost:9200/data/_doc/_bulk' --data-binary @accounts.json
        
- Run to search:

        curl -X GET \
          'http://localhost:9000/search?pattern=Simplon'
          
- Run to upload:

        curl --request POST \
          --url http://localhost:9000/upload \
          --header 'Content-Type: application/json' \
          --data '[{ "name": "foo" },{ "name": "bar" }]'
          
# Run front-end

- `cd fe`
- `npm i`
- `npm run start`
