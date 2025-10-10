*** Settings ***
Library    RequestsLibrary
Library    Collections
Library    DateTime
Library    FakerLibrary

*** Variables ***
# Configurações Base da API
${BASE_URL}             https://api-ticket-sales-staging.up.railway.app/api
${ENDPOINT}             /event
${HEADERS}              Content-Type=application/json

# Dados de Teste Comuns (Sucesso)
${FUTURE_DATE_INITIAL}  2030-10-10T18:00:00
${FUTURE_DATE_FINAL}    2030-10-10T21:00:00
${SUCCESS_LOCATION}     Sala Alpha
${SUCCESS_CAPACITY}     100

# Tags para execução seletiva em pipeline
${TAG_ALL_TESTS}        POST_EVENT

# Códigos de Status Esperados
${STATUS_201}           201
${STATUS_400}           400
${STATUS_409}           409
${STATUS_422}           422