*** Settings ***
Library    RequestsLibrary
Library    Collections
Library    DateTime
# Importa o arquivo de Keywords que contém a lógica dos testes
Resource   script_api_event.robot

*** Variables ***
# Configurações Base da API
${BASE_URL}    %{BASE_URL}
${ENDPOINT}    /api/event
&{HEADERS}     content-type=application/json

# Dados de Teste Comuns (Sucesso)
${FUTURE_DATE_INITIAL}  2030-10-10T18:00:00
${FUTURE_DATE_FINAL}    2030-10-10T21:00:00
${SUCCESS_NAME}         Evento de Lançamento
${SUCCESS_LOCATION}     Sala Alpha
${SUCCESS_CAPACITY}     100

# Tags para execução seletiva em pipeline
${TAG_ALL_TESTS}        POST_EVENT

# Códigos de Status Esperados
${STATUS_201}           201
${STATUS_400}           400
${STATUS_422}           422
