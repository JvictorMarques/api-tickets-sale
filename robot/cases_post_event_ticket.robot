*** Settings ***
Resource        resource_api_event.robot
Resource        script_api_event.robot

Suite Setup     Setup Suite Para Tickets

*** Keywords ***
Setup Suite Para Tickets
    [Documentation]    Cria a sessao API para os testes de ticket
    Criar Sessao API

*** Test Cases ***
POST-EVENT-TICKET-001 - Validar cadastro de novo evento com cadastro de novo tipo de ingresso para o mesmo
    [Tags]          ${TAG_ALL_TESTS}    SUCESSO    INICIAL
    [Documentation]    Cadastra evento e primeiro ingresso (capacidade 30)
    
    ${event_id}=    Criar Evento Base Para Tickets
    Set Global Variable     ${GLOBAL_EVENT_ID}     ${event_id}
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    Set Global Variable     ${GLOBAL_TICKET_NAME_1}    ${ticket_name}
    &{payload}=     Criar Payload Ticket Basico    ${ticket_name}    ${120}    ${30}
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${201}
    Validar Response Ticket Sucesso 201      ${response}     ${ticket_name}    ${event_id}

POST-EVENT-TICKET-002 - Validar cadastro de novo tipo ingresso para o evento anterior com capacidade complementar
    [Tags]          ${TAG_ALL_TESTS}    SUCESSO    COMPLEMENTAR
    [Documentation]    Cadastra segundo ingresso (capacidade 20) - Total: 50/100
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    Set Global Variable     ${GLOBAL_TICKET_NAME_2}    ${ticket_name}
    &{payload}=     Criar Payload Ticket Basico    ${ticket_name}    ${240}    ${20}
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${201}
    Validar Response Ticket Sucesso 201      ${response}     ${ticket_name}    ${event_id}


POST-EVENT-TICKET-003 - Validar impossibilidade de cadastro de novo tipo de ingresso com capacidade superior
    [Tags]          ${TAG_ALL_TESTS}    FALHA    CAPACIDADE_EXCEDIDA
    [Documentation]    Tenta cadastrar 60 ingressos - Limite: 50/100 - Excede: 10
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    ${ticket_name}=     FakerLibrary.Word
    &{payload}=     Criar Payload Ticket Basico    Social    ${120}     ${60}
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${409}
    
POST-EVENT-TICKET-004 - Validar impossibilidade de cadastro com data de início superior à do evento
    [Tags]          ${TAG_ALL_TESTS}    FALHA    DATA_INVALIDA
    [Documentation]    Tenta cadastrar com data inicial após término do evento
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    ${data_venda_invalida}=     Set Variable    2030-10-10T22:00:00
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    &{payload}=     Criar Payload Ticket Com Datas    ${ticket_name}   ${130}    ${20}    ${data_venda_invalida}    ${FUTURE_DATE_FINAL}
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${422}
    


POST-EVENT-TICKET-005 - Validar impossibilidade de cadastro com nome de tipo de ingresso duplicado
    [Tags]          ${TAG_ALL_TESTS}    FALHA    NOME_DUPLICADO
    [Documentation]    Tenta cadastrar com nome já existente (do cenário 001)
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    &{payload}=     Criar Payload Ticket Basico    ${GLOBAL_TICKET_NAME_1}    ${130}    ${10}
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${409}

POST-EVENT-TICKET-006 - Validar impossibilidade de cadastro para evento inexistente
    [Tags]          ${TAG_ALL_TESTS}    FALHA    EVENTO_INEXISTENTE
    [Documentation]    Tenta cadastrar para UUID que não existe
    
    ${uuid_inexistente}=    Set Variable    12345678-0000-0000-0000-000000000000
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    &{payload}=     Criar Payload Ticket Basico    ${ticket_name}   ${120}    ${10}
    ${response}=    Executar Post Ticket Para Evento    ${uuid_inexistente}     ${payload}      ${404}


POST-EVENT-TICKET-007 - Validar cadastro com capacidade complementar e data de início no dia do evento
    [Tags]          ${TAG_ALL_TESTS}    SUCESSO    DATA_EVENTO
    [Documentation]    Cadastra último lote (50) com data inicial igual à do evento
    
    ${event_id}=    Set Variable    ${GLOBAL_EVENT_ID}
    ${ticket_name}=     Gerar Nome Ticket Aleatorio
    &{payload}=     Criar Payload Ticket Com Datas    ${ticket_name}  ${240}   ${50}    ${FUTURE_DATE_INITIAL}    ${FUTURE_DATE_FINAL}
    ${response}=    Executar Post Ticket Para Evento    ${event_id}     ${payload}      ${201}
    Validar Response Ticket Sucesso 201      ${response}     ${ticket_name}    ${event_id}